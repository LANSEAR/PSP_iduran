package org.example.ui

import androidx.compose.runtime.mutableStateListOf
import org.example.logic.execution.CommandFactory
import org.example.logic.execution.ProcessRunner
import org.example.logic.model.Task
import org.example.logic.model.TaskActionType
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.timer

class TaskViewModel {

    // 🔹 Representación de una tarea
    data class TaskUi(
        val id: String = UUID.randomUUID().toString(),
        var name: String,
        var actionType: TaskActionType,
        var startTime: String,
        var endTime: String,
        var isRunning: Boolean = false,
        var isScheduled: Boolean = false,
        var repeatInterval: Long = 0L // en segundos
    )

    // 🔹 Representación de un log expandible
    data class LogEntry(
        val message: String,
        val details: String = "",
        val isError: Boolean = false,
        val timestamp: String = currentTimestamp()
    )

    // 🔹 Listas observables para Compose
    var tasks = mutableStateListOf<TaskUi>()
        private set

    var logs = mutableStateListOf<LogEntry>()
        private set

    // 🔹 Timers activos para tareas programadas
    private val activeTimers = mutableMapOf<String, Timer>()

    init {
        // Tarea de ejemplo inicial
        tasks.add(
            TaskUi(
                name = "Tarea 1",
                actionType = TaskActionType.CLEAN_TEMP,
                startTime = "08:00",
                endTime = "10:00"
            )
        )
    }

    fun addTask(
        name: String,
        functionType: TaskActionType,
        startTime: String,
        endTime: String
    ) {
        val newTask = TaskUi(
            name = name,
            actionType = functionType,
            startTime = startTime,
            endTime = endTime
        )
        tasks.add(newTask)
    }

    fun removeTaskById(id: String) {
        stopScheduledTask(id) // cancelar si estaba programada
        tasks.removeAll { it.id == id }
    }

    fun updateTask(
        id: String,
        name: String,
        functionType: TaskActionType,
        startTime: String,
        endTime: String
    ) {
        val task = tasks.find { it.id == id } ?: return
        task.name = name
        task.actionType = functionType
        task.startTime = startTime
        task.endTime = endTime
    }

    // 🔹 Ejecuta una tarea y guarda logs detallados
    fun runTaskNow(id: String, customArg: String? = null) {
        val taskUi = tasks.find { it.id == id } ?: return

        if (taskUi.isRunning) {
            addLog("⚠️ La tarea '${taskUi.name}' ya está en ejecución.")
            return
        }

        taskUi.isRunning = true
        addLog("▶️ Ejecutando tarea: ${taskUi.name}")

        Thread {
            try {
                val command = CommandFactory.buildCommandFor(taskUi.actionType, customArg)
                val result = ProcessRunner.runCommand(command)
                taskUi.isRunning = false

                when {
                    result.timedOut -> addLog(
                        "⏰ ${taskUi.name}: tiempo de ejecución superado.",
                        result.stderr.ifBlank { result.stdout },
                        isError = true
                    )
                    result.exitCode == 0 -> addLog(
                        "✅ ${taskUi.name} completada correctamente.",
                        result.stdout
                    )
                    else -> addLog(
                        "❌ ${taskUi.name} falló (código ${result.exitCode})",
                        result.stderr.ifBlank { result.stdout },
                        isError = true
                    )
                }
            } catch (e: Exception) {
                taskUi.isRunning = false
                addLog("💥 Error ejecutando ${taskUi.name}: ${e.message}", isError = true)
            }
        }.start()
    }

    // 🔁 Programar ejecución automática dentro del horario permitido
    fun scheduleTask(id: String, intervalSeconds: Long) {
        val task = tasks.find { it.id == id } ?: return
        if (task.isScheduled) {
            addLog("⚠️ ${task.name} ya estaba programada.")
            return
        }

        task.isScheduled = true
        task.repeatInterval = intervalSeconds
        addLog("⏱️ ${task.name} programada cada ${intervalSeconds / 60} minutos.")

        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val timer = timer(
            name = "TaskTimer-${task.id}",
            initialDelay = 0L,
            period = intervalSeconds * 1000
        ) {
            try {
                val now = LocalTime.now()
                val start = LocalTime.parse(task.startTime, formatter)
                val end = LocalTime.parse(task.endTime, formatter)

                if (now.isAfter(start) && now.isBefore(end)) {
                    runTaskNow(task.id)
                } else {
                    addLog("⏸️ ${task.name} pausada (fuera del horario permitido).")
                }
            } catch (e: Exception) {
                addLog("⚠️ Error en programación de ${task.name}: ${e.message}", isError = true)
            }
        }

        activeTimers[id] = timer
    }

    // 🛑 Detener programación
    fun stopScheduledTask(id: String) {
        val task = tasks.find { it.id == id } ?: return
        if (!task.isScheduled) {
            addLog("ℹ️ ${task.name} no estaba programada.")
            return
        }

        activeTimers[id]?.cancel()
        activeTimers.remove(id)

        task.isScheduled = false
        addLog("⏹️ Programación detenida para ${task.name}.")
    }

    // 🧾 Logs
    private fun addLog(message: String, details: String = "", isError: Boolean = false) {
        logs.add(0, LogEntry(message, details, isError))
    }

    // 🕒 Marca temporal
    companion object {
        private fun currentTimestamp(): String {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return sdf.format(Date())
        }
    }
}

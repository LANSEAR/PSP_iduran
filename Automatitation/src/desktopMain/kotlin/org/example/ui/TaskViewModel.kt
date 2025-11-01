package org.example.ui

import androidx.compose.runtime.mutableStateListOf
import org.example.logic.execution.CommandFactory
import org.example.logic.execution.ProcessRunner
import org.example.logic.execution.TaskScheduler
import org.example.logic.model.Task
import org.example.logic.model.TaskActionType
import java.text.SimpleDateFormat
import java.util.*

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

        // Evita ejecuciones duplicadas
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
    fun scheduleTask(id: String, intervalSeconds: Long) {
        val taskUi = tasks.find { it.id == id } ?: return
        if (taskUi.isScheduled) {
            addLog("⚠️ ${taskUi.name} ya estaba programada.")
            return
        }

        taskUi.isScheduled = true
        taskUi.repeatInterval = intervalSeconds
        addLog("⏱️ ${taskUi.name} programada cada ${intervalSeconds}s.")

        TaskScheduler.scheduleTask(id, intervalSeconds) {
            runTaskNow(id)
        }
    }

    fun stopScheduledTask(id: String) {
        val taskUi = tasks.find { it.id == id } ?: return
        if (!taskUi.isScheduled) {
            addLog("ℹ️ ${taskUi.name} no estaba programada.")
            return
        }

        TaskScheduler.cancelTask(id)
        taskUi.isScheduled = false
        addLog("⏹️ Programación detenida para ${taskUi.name}.")
    }

    // 🔹 Añade logs con detalles y marca temporal
    private fun addLog(message: String, details: String = "", isError: Boolean = false) {
        logs.add(0, LogEntry(message, details, isError))
    }

    // 🔹 Hora actual formateada para los logs
    companion object {
        private fun currentTimestamp(): String {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return sdf.format(Date())
        }
    }
}

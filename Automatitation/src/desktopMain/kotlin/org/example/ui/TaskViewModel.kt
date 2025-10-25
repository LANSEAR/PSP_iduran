package org.example.ui

import androidx.compose.runtime.mutableStateListOf
import org.example.logic.execution.CommandFactory
import org.example.logic.execution.ProcessRunner
import org.example.logic.model.Task
import org.example.logic.model.TaskActionType
import java.util.UUID

class TaskViewModel {

    data class TaskUi(
        val id: String = UUID.randomUUID().toString(),
        var name: String,
        var actionType: TaskActionType,
        var startTime: String,
        var endTime: String
    )

    // Lista observable que pinta la UI
    var tasks = mutableStateListOf<TaskUi>()
        private set

    init {
        // tarea de ejemplo de arranque
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


    // 👇 Esto ya ejecuta una tarea ahora mismo (para pruebas)
    fun runTaskNow(id: String, customArg: String? = null): ProcessRunner.Result? {
        val taskUi = tasks.find { it.id == id } ?: return null

        // Creamos Task técnico a partir de TaskUi
        val command = CommandFactory.buildCommandFor(taskUi.actionType, customArg)
        val taskExec = Task(
            id = taskUi.id,
            name = taskUi.name,
            actionType = taskUi.actionType,
            command = command,
            startTime = taskUi.startTime,
            endTime = taskUi.endTime
        )

        // Ejecutamos
        return ProcessRunner.runCommand(taskExec.command)
    }
}

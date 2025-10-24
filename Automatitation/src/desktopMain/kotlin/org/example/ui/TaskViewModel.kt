package org.example.ui

import androidx.compose.runtime.mutableStateListOf

class TaskViewModel {
    var tasks = mutableStateListOf<TaskUi>(
        TaskUi(
            id = java.util.UUID.randomUUID().toString(),
            name = "TAREA 1",
            functionType = "Función de ejemplo",
            startTime = "08:00",
            endTime = "10:00"
        )
    )
        private set
    data class TaskUi(
        val id: String,
        val name: String,
        val functionType: String,
        val startTime: String,
        val endTime: String
    )

    fun addTask(name: String, functionType: String, startTime: String, endTime: String) {
        val newTask = TaskUi(
            id = java.util.UUID.randomUUID().toString(),
            name = name,
            functionType = functionType,
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
        newName: String? = null,
        newFunctionType: String? = null,
        newStart: String? = null,
        newEnd: String? = null
    ) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index == -1) return

        val oldTask = tasks[index]
        val updatedTask = oldTask.copy(
            name = newName ?: oldTask.name,
            functionType = newFunctionType ?: oldTask.functionType,
            startTime = newStart ?: oldTask.startTime,
            endTime = newEnd ?: oldTask.endTime
        )
        tasks[index] = updatedTask
    }

}

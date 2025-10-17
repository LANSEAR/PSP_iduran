package org.example

import androidx.compose.runtime.mutableStateListOf

class TaskViewModel {
    // Lista observable de tareas
    var tasks = mutableStateListOf("TAREA 1")
        private set

    fun addTask(name: String) {
        tasks.add(name)
    }

    fun removeTask(name: String) {
        tasks.remove(name)
    }
}

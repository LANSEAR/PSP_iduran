package org.example.logic.execution

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object TaskScheduler {
    private val scheduler = Executors.newScheduledThreadPool(4)
    private val scheduledTasks = mutableMapOf<String, ScheduledFuture<*>>()

    fun scheduleTask(
        id: String,
        intervalSeconds: Long,
        taskBlock: () -> Unit
    ) {
        cancelTask(id) // si ya estaba programada, se reemplaza
        val future = scheduler.scheduleAtFixedRate(taskBlock, 0, intervalSeconds, TimeUnit.SECONDS)
        scheduledTasks[id] = future
    }

    fun cancelTask(id: String) {
        scheduledTasks[id]?.cancel(true)
        scheduledTasks.remove(id)
    }

    fun cancelAll() {
        scheduledTasks.values.forEach { it.cancel(true) }
        scheduledTasks.clear()
    }
}

package org.example.logic.execution

import org.example.logic.model.TaskActionType
import org.example.ui.TaskViewModel
import kotlin.concurrent.thread


/**
 * Controlador principal encargado de ejecutar tareas según su tipo.
 * Usa OsDetector, CommandFactory y ProcessRunner.
 */
object TaskExecutor {

    /**
     * Ejecuta una tarea completa en un hilo separado.
     */
    fun runTask(task: TaskViewModel.TaskUi) {
        thread(name = "TaskExecutor-${task.name}") {
            try {
                println("🔹 Ejecutando tarea: ${task.name} [${task.actionType}]")

                // Detectar el sistema operativo actual
                val os = OsDetector.current
                println("🧭 Sistema operativo detectado: $os")

                // Generar el comando adecuado según el tipo de tarea
                val command = CommandFactory.buildCommandFor(task.actionType)
                println("🧩 Comando generado: $command")

                // Ejecutar el proceso y capturar salida y errores
                val result = ProcessRunner.run(command)
                println("✅ Resultado de la tarea '${task.name}':")
                println(result)

            } catch (e: Exception) {
                println("❌ Error ejecutando tarea '${task.name}': ${e.message}")
                e.printStackTrace()
            }
        }
    }

    /**
     * Ejecuta directamente una acción concreta sin TaskUi (modo simple o prueba).
     */
    fun runAction(type: TaskActionType) {
        val task = TaskViewModel.TaskUi(
            name = type.name,
            actionType = type,
            startTime = "",
            endTime = ""
        )
        runTask(task)
    }
}
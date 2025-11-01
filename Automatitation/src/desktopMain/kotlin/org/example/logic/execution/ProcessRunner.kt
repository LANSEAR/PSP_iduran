package org.example.logic.execution

import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.Date

object ProcessRunner {

    data class Result(
        val exitCode: Int,
        val stdout: String,
        val stderr: String,
        val timedOut: Boolean
    )

    // 🔹 Función principal que ejecuta comandos o tareas internas
    fun runCommand(
        command: List<String>,
        workingDir: File? = null,
        timeoutSeconds: Long = 30
    ): Result {

        if (command.isEmpty()) {
            return Result(
                exitCode = -1,
                stdout = "",
                stderr = "⚠️ Comando vacío o no soportado en este sistema",
                timedOut = false
            )
        }

        // 🧩 Comando interno para copia recursiva (sin usar cmd/bash)
        if (command.firstOrNull() == "internal_backup") {
            val source = File(command.getOrNull(1) ?: return Result(-1, "", "Ruta de origen no válida", false))
            val destination = File(command.getOrNull(2) ?: return Result(-1, "", "Ruta de destino no válida", false))

            return try {
                if (!source.exists()) {
                    return Result(-1, "", "La carpeta de origen no existe.", false)
                }

                val copiedCount = copyDirectoryRecursively(source, destination)

                Result(
                    exitCode = 0,
                    stdout = "✅ Copia completada correctamente.\nSe copiaron $copiedCount elementos desde '${source.absolutePath}' a '${destination.absolutePath}'",
                    stderr = "",
                    timedOut = false
                )
            } catch (e: Exception) {
                Result(
                    exitCode = -1,
                    stdout = "",
                    stderr = "❌ Error en la copia: ${e.message}",
                    timedOut = false
                )
            }
        }

        // 🧾 Generación interna de informe (modo nativo)
        if (command.firstOrNull() == "internal_report") {
            val baseName = command.getOrNull(1)?.replace(" ", "_") ?: "informe"
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val reportDir = File("reports")

            if (!reportDir.exists()) reportDir.mkdirs()
            val reportFile = File(reportDir, "${baseName}_$timestamp.txt")

            return try {
                val osName = System.getProperty("os.name")
                val user = System.getProperty("user.name")
                val javaVer = System.getProperty("java.version")
                val dateNow = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())

                val reportContent = buildString {
                    appendLine("===== INFORME AUTOMÁTICO DEL SISTEMA =====")
                    appendLine("📅 Fecha: $dateNow")
                    appendLine("👤 Usuario: $user")
                    appendLine("💻 Sistema operativo: $osName")
                    appendLine("☕ Versión de Java: $javaVer")
                    appendLine("-------------------------------------------")
                    appendLine("Resumen de entorno de ejecución:")
                    appendLine("- Carpeta de trabajo: ${File(".").absolutePath}")
                    appendLine("- Generado por: Automatizador de Tareas Kotlin")
                    appendLine("-------------------------------------------")
                    appendLine("Notas:")
                    appendLine("Este informe fue generado automáticamente por el sistema de tareas.")
                }

                reportFile.writeText(reportContent)

                Result(
                    exitCode = 0,
                    stdout = "✅ Informe generado correctamente en: ${reportFile.absolutePath}",
                    stderr = "",
                    timedOut = false
                )
            } catch (e: Exception) {
                Result(
                    exitCode = -1,
                    stdout = "",
                    stderr = "❌ Error generando el informe: ${e.message}",
                    timedOut = false
                )
            }
        }

        // 🔸 Ejecución normal del sistema
        return try {
            val pb = ProcessBuilder(command)
            if (workingDir != null) pb.directory(workingDir)

            val process = pb.start()

            // Leer stdout y stderr sin bloquear
            val pool = Executors.newFixedThreadPool(2)
            val stdoutFuture = pool.submit<String> { process.inputStream.bufferedReader().readText() }
            val stderrFuture = pool.submit<String> { process.errorStream.bufferedReader().readText() }

            val finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS)
            val timedOut = !finished

            if (timedOut) process.destroyForcibly()

            val outText = stdoutFuture.get(1, TimeUnit.SECONDS)
            val errText = stderrFuture.get(1, TimeUnit.SECONDS)

            pool.shutdownNow()

            val exitCode = if (timedOut) -1 else process.exitValue()

            Result(exitCode, outText.trim(), errText.trim(), timedOut)
        } catch (e: Exception) {
            Result(
                exitCode = -1,
                stdout = "",
                stderr = "❌ Error al ejecutar el proceso: ${e.message}",
                timedOut = false
            )
        }
    }

    // 🔸 Alias simple (mantiene compatibilidad con TaskExecutor)
    fun run(command: List<String>): String {
        val result = runCommand(command)
        return buildString {
            appendLine("🟢 Salida estándar:\n${result.stdout}")
            if (result.stderr.isNotBlank()) appendLine("🔴 Error:\n${result.stderr}")
            if (result.timedOut) appendLine("⏰ El proceso superó el tiempo límite.")
            appendLine("Código de salida: ${result.exitCode}")
        }.trim()
    }

    // 🧩 Copia recursiva de carpetas y ficheros
    private fun copyDirectoryRecursively(source: File, destination: File): Int {
        var count = 0
        if (!source.exists()) return 0

        if (source.isDirectory) {
            if (!destination.exists()) destination.mkdirs()
            source.listFiles()?.forEach { child ->
                val destChild = File(destination, child.name)
                count += copyDirectoryRecursively(child, destChild)
            }
        } else {
            source.copyTo(destination, overwrite = true)
            count++
        }
        return count
    }
}

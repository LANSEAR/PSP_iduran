package org.example.logic.execution

import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ProcessRunner {

    data class Result(
        val exitCode: Int,
        val stdout: String,
        val stderr: String,
        val timedOut: Boolean
    )

    // 🔹 Función principal que ejecuta comandos del sistema
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

    // 🔸 Alias compatible con tu TaskExecutor (para usar ProcessRunner.run(command))
    fun run(command: List<String>): String {
        val result = runCommand(command)
        return buildString {
            appendLine("🟢 Salida estándar:\n${result.stdout}")
            if (result.stderr.isNotBlank()) appendLine("🔴 Error:\n${result.stderr}")
            if (result.timedOut) appendLine("⏰ El proceso superó el tiempo límite.")
            appendLine("Código de salida: ${result.exitCode}")
        }.trim()
    }
}

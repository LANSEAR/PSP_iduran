package org.example.logic.execution

import org.example.logic.model.OsType
import org.example.logic.model.TaskActionType

object CommandFactory {

    fun buildCommandFor(
        action: TaskActionType,
        customArg: String? = null
    ): List<String> {
        return when (action) {

            // 🧹 Limpiar temporales
            TaskActionType.CLEAN_TEMP -> when (OsDetector.current) {
                OsType.WINDOWS -> listOf("cmd.exe", "/c", "del /q/f/s %TEMP%\\*")
                OsType.LINUX, OsType.MAC -> listOf("bash", "-c", "rm -rf /tmp/*")
                else -> emptyList()
            }

            // 🧾 Generar informe (interno)
            TaskActionType.GENERATE_REPORT -> {
                // Generación gestionada por ProcessRunner
                listOf("internal_report", customArg ?: "informe_automatizador")
            }

            // 📦 Copia de seguridad recursiva (interna)
            TaskActionType.BACKUP_FOLDER -> {
                val parts = customArg?.split("|")
                val source = parts?.getOrNull(0)
                val dest = parts?.getOrNull(1)

                if (source.isNullOrBlank() || dest.isNullOrBlank()) {
                    return emptyList()
                }

                listOf("internal_backup", source, dest)
            }
            TaskActionType.RUN_SCRIPT -> {
                if (customArg.isNullOrBlank()) return emptyList()

                when (OsDetector.current) {
                    OsType.WINDOWS -> listOf("cmd.exe", "/c", customArg)
                    OsType.LINUX, OsType.MAC -> listOf("bash", customArg)
                    else -> emptyList()
                }
            }

        }
    }
}

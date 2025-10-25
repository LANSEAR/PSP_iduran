package org.example.logic.execution

import org.example.logic.model.OsType
import org.example.logic.model.TaskActionType

object CommandFactory {

    // Devuelve la lista de strings que debe ejecutar ProcessBuilder
    fun buildCommandFor(
        action: TaskActionType,
        customArg: String? = null // p.e. ruta origen/destino en backups
    ): List<String> {
        return when (action) {

            TaskActionType.CLEAN_TEMP -> when (OsDetector.current) {
                OsType.WINDOWS ->
                    listOf("cmd.exe", "/c", "del /q/f/s %TEMP%\\*")
                OsType.LINUX, OsType.MAC ->
                    listOf("bash", "-c", "rm -rf /tmp/*")
                else ->
                    emptyList()
            }

            TaskActionType.BACKUP_FOLDER -> when (OsDetector.current) {
                OsType.WINDOWS ->
                    // ejemplo simple: copia recursiva de una carpeta a otra
                    // customArg podría ser algo estilo "C:\\origen|C:\\destino"
                    listOf("cmd.exe", "/c", "echo Backup no implementado aún en Windows")
                OsType.LINUX, OsType.MAC ->
                    listOf("bash", "-c", "echo Backup no implementado aún en Unix")
                else ->
                    emptyList()
            }

            TaskActionType.CUSTOM_COMMAND -> when (OsDetector.current) {
                OsType.WINDOWS ->
                    listOf("cmd.exe", "/c", customArg ?: "echo sin comando")
                OsType.LINUX, OsType.MAC ->
                    listOf("bash", "-c", customArg ?: "echo sin comando")
                else ->
                    emptyList()
            }
        }
    }
}
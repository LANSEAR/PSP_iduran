package org.example.logic.model

object TaskActionMapper {

    private val displayNames = mapOf(
        TaskActionType.CLEAN_TEMP to "Limpiar temporales",
        TaskActionType.GENERATE_REPORT to "Generar informe",
        TaskActionType.BACKUP_FOLDER to "Copia de seguridad"
    )

    fun getDisplayName(type: TaskActionType): String =
        displayNames[type] ?: "Desconocido"

    fun fromDisplayName(name: String): TaskActionType =
        displayNames.entries.firstOrNull { it.value == name }?.key ?: TaskActionType.CLEAN_TEMP

    fun allDisplayNames(): List<String> = displayNames.values.toList()
}

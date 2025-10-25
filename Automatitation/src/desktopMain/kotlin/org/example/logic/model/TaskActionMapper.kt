package org.example.logic.model

object TaskActionMapper {

    private val displayNames = mapOf(
        TaskActionType.BACKUP_FOLDER to "Copia de seguridad",
        TaskActionType.CLEAN_TEMP to "Limpiar temporales",
        TaskActionType.CUSTOM_COMMAND to "Generar informe"
    )

    fun getDisplayName(type: TaskActionType): String =
        displayNames[type] ?: type.name

    fun fromDisplayName(name: String): TaskActionType =
        displayNames.entries.firstOrNull { it.value == name }?.key ?: TaskActionType.CUSTOM_COMMAND

    fun allDisplayNames(): List<String> = displayNames.values.toList()
}
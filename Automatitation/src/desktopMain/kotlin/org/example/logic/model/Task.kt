package org.example.logic.model

enum class OsType { WINDOWS, LINUX, MAC, UNKNOWN }

enum class TaskActionType {
    CLEAN_TEMP,        // limpiar temporales
    BACKUP_FOLDER,     // copiar carpeta a otra ruta
    CUSTOM_COMMAND     // comando definido por el usuario
}

data class Task(
    val id: String,
    var name: String,

    // Tipo de acción lógica (lo que eliges en el combo "Función")
    var actionType: TaskActionType,

    // Comando real que se va a ejecutar
    var command: List<String>,

    // Ventana horaria, por ahora solo informativa
    var startTime: String,
    var endTime: String
)
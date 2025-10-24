package org.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DialogBorrarTarea(vm: TaskViewModel, selected: TaskViewModel.TaskUi?, onDismiss: () -> Unit) {
    var selectedTask by remember { mutableStateOf(selected) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Borrar tarea") },
        text = {
            if (vm.tasks.isEmpty()) Text("No hay tareas disponibles.")
            else Column {
                vm.tasks.forEach { t ->
                    Text(
                        t.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedTask = t }
                            .padding(4.dp),
                        color = if (selectedTask == t) Color.Red else Color.Unspecified
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                selectedTask?.let { vm.removeTaskById(it.id) }
                onDismiss()
            }) { Text("Borrar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

package org.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

// 🎨 Colores corporativos
private val RedPrimary = Color(0xFFBA3023)
private val NeutralDark = Color(0xFF1C1C1C)
private val NeutralLight = Color(0xFFF5F5F5)

@Composable
fun DialogSeleccionarTarea(
    vm: TaskViewModel,
    onDismiss: () -> Unit,
    onTaskSelected: (TaskViewModel.TaskUi) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Selecciona una tarea para editar",
                style = MaterialTheme.typography.titleLarge,
                color = RedPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (vm.tasks.isEmpty()) {
                    Text("No hay tareas disponibles", color = NeutralDark)
                } else {
                    vm.tasks.forEach { task ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onTaskSelected(task)
                                },
                            color = NeutralLight,
                            tonalElevation = 2.dp,
                            shadowElevation = 2.dp
                        ) {
                            Text(
                                text = task.name,
                                modifier = Modifier.padding(10.dp),
                                color = NeutralDark,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = RedPrimary)
            }
        },
        containerColor = NeutralLight
    )
}

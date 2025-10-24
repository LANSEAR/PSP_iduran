package org.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogCrearTarea(vm: TaskViewModel, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var function by remember { mutableStateOf("Copia de seguridad") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }

    val funciones = listOf("Copia de seguridad", "Limpiar temporales", "Generar informe")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear nueva tarea") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                Spacer(Modifier.height(8.dp))
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = function,
                        onValueChange = {},
                        label = { Text("Función") },
                        readOnly = true,
                        modifier = Modifier.menuAnchor()
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        funciones.forEach { f ->
                            DropdownMenuItem(text = { Text(f) }, onClick = {
                                function = f
                                expanded = false
                            })
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Hora inicio (HH:MM)") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("Hora fin (HH:MM)") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank()) {
                    vm.addTask(name, function, start, end)
                    onDismiss()
                }
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

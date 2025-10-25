package org.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.logic.model.TaskActionMapper
import org.example.logic.model.TaskActionType

// 🎨 Paleta corporativa
private val RedPrimary = Color(0xFFBA3023)
private val CyanAccent = Color(0xFF1FC2D3)
private val NeutralLight = Color(0xFFF5F5F5)
private val NeutralDark = Color(0xFF1C1C1C)
private val TextPrimary = Color(0xFF1C1C1C)
private val LabelGray = Color(0xFF5F5F5F)
private val SelectedBg = Color(0xFFFFEDED) // Fondo rojo claro al seleccionar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogCrearTarea(
    vm: TaskViewModel,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var function by remember { mutableStateOf(TaskActionType.CUSTOM_COMMAND) }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }

    // 🎨 Colores coherentes
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = RedPrimary,
        unfocusedBorderColor = LabelGray,
        focusedLabelColor = RedPrimary,
        unfocusedLabelColor = LabelGray,
        cursorColor = RedPrimary,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Crear nueva tarea",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = RedPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 🏷️ Nombre de la tarea
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la tarea") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors
                )

                // ⚙️ Función (usando TaskActionMapper)
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = TaskActionMapper.getDisplayName(function),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Función") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = fieldColors
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(NeutralLight)
                    ) {
                        TaskActionMapper.allDisplayNames().forEach { displayName ->
                            val isSelected = TaskActionMapper.getDisplayName(function) == displayName
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        displayName,
                                        color = if (isSelected) RedPrimary else TextPrimary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                onClick = {
                                    function = TaskActionMapper.fromDisplayName(displayName)
                                    expanded = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (isSelected) SelectedBg else Color.Transparent),
                                colors = MenuDefaults.itemColors(
                                    textColor = TextPrimary
                                )
                            )
                        }
                    }
                }

                // ⏰ Horario
                OutlinedTextField(
                    value = start,
                    onValueChange = { start = it },
                    label = { Text("Hora de inicio (HH:MM)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors
                )

                OutlinedTextField(
                    value = end,
                    onValueChange = { end = it },
                    label = { Text("Hora de fin (HH:MM)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = RedPrimary)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        vm.addTask(name, function, start, end)
                        onDismiss()
                    }
                }
            ) {
                Text("Guardar", color = CyanAccent)
            }
        },
        containerColor = NeutralLight,
        tonalElevation = 8.dp
    )
}

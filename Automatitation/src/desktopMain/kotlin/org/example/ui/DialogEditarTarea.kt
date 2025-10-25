package org.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
private val SelectedBg = Color(0xFFFFEDED) // Suave rojo claro para resaltar selección

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogEditarTarea(
    vm: TaskViewModel,
    selected: TaskViewModel.TaskUi?,
    onDismiss: () -> Unit
) {
    if (selected == null) return

    var name by remember { mutableStateOf(selected.name) }
    var function by remember { mutableStateOf(selected.actionType) }
    var start by remember { mutableStateOf(selected.startTime) }
    var end by remember { mutableStateOf(selected.endTime) }

    // 🎨 Colores coherentes con la UI de creación
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
                text = "⚙️ Ajustes de tarea",
                color = RedPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // 🏷️ Nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la tarea") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors
                )

                // ⚙️ Función / tipo (usando TaskActionMapper)
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
        confirmButton = {
            TextButton(
                onClick = {
                    vm.updateTask(selected.id, name, function, start, end)
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(contentColor = CyanAccent)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = RedPrimary)
            ) {
                Text("Cancelar")
            }
        },
        containerColor = NeutralLight,
        tonalElevation = 8.dp
    )
}

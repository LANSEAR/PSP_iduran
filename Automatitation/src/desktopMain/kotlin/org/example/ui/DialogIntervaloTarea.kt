package org.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// 🎨 Colores corporativos coherentes
private val RedPrimary = Color(0xFFBA3023)
private val CyanAccent = Color(0xFF1FC2D3)
private val NeutralLight = Color(0xFFF5F5F5)
private val NeutralDark = Color(0xFF1C1C1C)

@Composable
fun DialogIntervaloTarea(
    vm: TaskViewModel,
    task: TaskViewModel.TaskUi,
    onDismiss: () -> Unit
) {
    var selectedInterval by remember { mutableStateOf<Long?>(null) }

    val intervalos = listOf(
        15L * 60 to "Cada 15 minutos",
        30L * 60 to "Cada 30 minutos",
        60L * 60 to "Cada 1 hora",
        3L * 60 * 60 to "Cada 3 horas",
        24L * 60 * 60 to "Cada 24 horas"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    selectedInterval?.let { vm.scheduleTask(task.id, it) }
                    onDismiss()
                }
            ) {
                Text("Confirmar", color = RedPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = RedPrimary)
            }
        },
        title = {
            Text(
                "Seleccionar intervalo",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = RedPrimary,
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "¿Cada cuánto quieres ejecutar la tarea?",
                    color = NeutralDark,
                    style = MaterialTheme.typography.bodyLarge
                )

                intervalos.forEach { (seconds, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedInterval == seconds,
                            onClick = { selectedInterval = seconds },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = RedPrimary,
                                unselectedColor = NeutralDark
                            )
                        )
                        Text(
                            label,
                            color = NeutralDark,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        },
        containerColor = NeutralLight,
        tonalElevation = 8.dp
    )
}

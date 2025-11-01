package org.example.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DialogIntervaloTarea(
    vm: TaskViewModel,
    task: TaskViewModel.TaskUi,
    onDismiss: () -> Unit
) {
    var selectedOption by remember { mutableStateOf("1 hora") }

    val options = listOf(
        "Cada 15 minutos" to 900L,
        "Cada 30 minutos" to 1800L,
        "Cada 1 hora" to 3600L,
        "Cada 3 horas" to 10800L,
        "Cada 24 horas" to 86400L
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val interval = options.first { it.first == selectedOption }.second
                    vm.scheduleTask(task.id, interval)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA3023))
            ) {
                Text("Confirmar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        title = { Text("Seleccionar intervalo", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("¿Cada cuánto quieres ejecutar la tarea?", color = Color.DarkGray)
                options.forEach { (label, _) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOption = label }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedOption == label,
                            onClick = { selectedOption = label }
                        )
                        Text(label, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        containerColor = Color(0xFFF5F5F5)
    )
}

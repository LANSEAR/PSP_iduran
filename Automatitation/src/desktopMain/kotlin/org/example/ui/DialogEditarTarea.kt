package org.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DialogEditarTarea(vm: TaskViewModel, selected: TaskViewModel.TaskUi?, onDismiss: () -> Unit) {
    if (selected == null) return

    var name by remember { mutableStateOf(selected.name) }
    var function by remember { mutableStateOf(selected.functionType) }
    var start by remember { mutableStateOf(selected.startTime) }
    var end by remember { mutableStateOf(selected.endTime) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar tarea") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = function, onValueChange = { function = it }, label = { Text("Función") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Hora inicio") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("Hora fin") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                vm.updateTask(selected.id, name, function, start, end)
                onDismiss()
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

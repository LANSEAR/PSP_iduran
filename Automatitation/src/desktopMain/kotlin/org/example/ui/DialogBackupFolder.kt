package org.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import javax.swing.JFileChooser
import javax.swing.SwingUtilities

@Composable
fun DialogBackupFolder(
    vm: TaskViewModel,
    task: TaskViewModel.TaskUi,
    onDismiss: () -> Unit
) {
    var sourcePath by remember { mutableStateOf("") }
    var destPath by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) } // 🔹 Estado del error

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    // Validación antes de ejecutar
                    if (sourcePath.isBlank() || destPath.isBlank()) {
                        showError = true
                    } else {
                        val arg = "$sourcePath|$destPath"
                        vm.runTaskNow(task.id, arg)
                        onDismiss()
                    }
                }
            ) {
                Text("Iniciar copia")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Configurar copia de seguridad") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                // --- Carpeta ORIGEN ---
                OutlinedTextField(
                    value = sourcePath,
                    onValueChange = { sourcePath = it },
                    label = { Text("Carpeta de origen") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val chosen = selectFolderSwing("Seleccionar carpeta de origen")
                        if (chosen.isNotBlank()) {
                            sourcePath = chosen
                            showError = false
                        }
                    }
                ) {
                    Text("Elegir carpeta origen…")
                }

                // --- Carpeta DESTINO ---
                OutlinedTextField(
                    value = destPath,
                    onValueChange = { destPath = it },
                    label = { Text("Carpeta de destino") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val chosen = selectFolderSwing("Seleccionar carpeta de destino")
                        if (chosen.isNotBlank()) {
                            destPath = chosen
                            showError = false
                        }
                    }
                ) {
                    Text("Elegir carpeta destino…")
                }

                // 🔴 Mensaje de error
                if (showError) {
                    Text(
                        "⚠️ Debes seleccionar ambas carpetas antes de continuar.",
                        color = Color.Red,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Text(
                    "Selecciona las carpetas de origen y destino para realizar la copia.",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    )
}

/**
 * Abre un selector nativo de carpetas usando Swing JFileChooser en modo DIRECTORIES_ONLY.
 */
fun selectFolderSwing(titleText: String): String {
    var resultPath = ""

    val runnable = Runnable {
        val chooser = JFileChooser().apply {
            dialogTitle = titleText
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            isAcceptAllFileFilterUsed = false
        }

        val result = chooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            resultPath = chooser.selectedFile.absolutePath
        }
    }

    if (SwingUtilities.isEventDispatchThread()) {
        runnable.run()
    } else {
        SwingUtilities.invokeAndWait(runnable)
    }

    return resultPath
}

package org.example.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.logic.model.TaskActionType

// 🎨 Paleta corporativa
private val RedPrimary = Color(0xFFBA3023)
private val CyanAccent = Color(0xFF1FC2D3)
private val OrangeBright = Color(0xFFF76B35)
private val YellowAccent = Color(0xFFF7C300)
private val NeutralLight = Color(0xFFF5F5F5)
private val NeutralDark = Color(0xFF1C1C1C)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Automatizador de Tareas"
    ) {
        MaterialTheme(
            colorScheme = darkColorScheme(
                primary = RedPrimary,
                secondary = CyanAccent,
                background = NeutralLight,
                surface = NeutralLight,
                onPrimary = Color.White,
                onBackground = NeutralDark
            )
        ) {
            MainScreen(onExit = ::exitApplication)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
    vm: TaskViewModel = remember { TaskViewModel() },
    onExit: () -> Unit = {}
) {

    // Estados del menú principal
    var expandedPlus by remember { mutableStateOf(false) }
    var expandedDots by remember { mutableStateOf(false) }

    // Estados de diálogos
    var showCreateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showSelectDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showGuideDialog by remember { mutableStateOf(false) }

    // Estado para la tarea seleccionada
    var selectedTask by remember { mutableStateOf<TaskViewModel.TaskUi?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralLight)
            .padding(16.dp)
    ) {
        // 🧩 Barra superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ➕ Menú principal
            Box {
                Text(
                    "+",
                    color = RedPrimary,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.clickable { expandedPlus = true }
                )

                DropdownMenu(
                    expanded = expandedPlus,
                    onDismissRequest = { expandedPlus = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("CREAR TAREA", color = NeutralDark) },
                        onClick = {
                            expandedPlus = false
                            showCreateDialog = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("BORRAR TAREA", color = NeutralDark) },
                        onClick = {
                            expandedPlus = false
                            showDeleteDialog = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("EDITAR TAREA", color = NeutralDark) },
                        onClick = {
                            expandedPlus = false
                            showSelectDialog = true
                        }
                    )
                }
            }

            // ⋮ Menú lateral
            Box {
                Text(
                    "⋮",
                    color = RedPrimary,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.clickable { expandedDots = true }
                )
                DropdownMenu(
                    expanded = expandedDots,
                    onDismissRequest = { expandedDots = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("GUÍA", color = NeutralDark) },
                        onClick = {
                            expandedDots = false
                            showGuideDialog = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("INFO", color = NeutralDark) },
                        onClick = {
                            expandedDots = false
                            showInfoDialog = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("SALIR", color = OrangeBright) },
                        onClick = {
                            expandedDots = false
                            onExit()
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // 🟥 Contenedor de tareas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0))
                .padding(16.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                vm.tasks.forEach { task ->
                    var expandedTaskMenu by remember(task.id) { mutableStateOf(false) }
                    var showIntervalDialog by remember(task.id) { mutableStateOf(false) }
                    var showBackupDialog by remember(task.id) { mutableStateOf(false) }
                    var showScriptDialog by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(if (task.isScheduled) YellowAccent else RedPrimary)
                            .combinedClickable(
                                onClick = { expandedTaskMenu = true },
                                onLongClick = { expandedTaskMenu = true }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            if (task.isRunning) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(28.dp)
                                )
                            } else {
                                Text(
                                    text = task.name,
                                    color = NeutralLight,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (task.isScheduled && task.repeatInterval > 0) {
                                    val humanInterval = when (task.repeatInterval) {
                                        in 1..1800 -> "Cada ${task.repeatInterval / 60} min"
                                        in 1801..7200 -> "Cada ${(task.repeatInterval / 3600)} h"
                                        else -> "Cada ${(task.repeatInterval / 3600)} h aprox."
                                    }
                                    Text(
                                        text = "⏱️ $humanInterval",
                                        color = Color.White.copy(alpha = 0.9f),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }

                        // 🔧 Menú contextual
                        DropdownMenu(
                            expanded = expandedTaskMenu,
                            onDismissRequest = { expandedTaskMenu = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("▶ Ejecutar ahora", color = CyanAccent) },
                                onClick = {
                                    expandedTaskMenu = false
                                    when (task.actionType) {
                                        TaskActionType.BACKUP_FOLDER -> showBackupDialog = true
                                        else -> vm.runTaskNow(task.id)
                                    }
                                }
                            )
                            if (task.actionType == TaskActionType.RUN_SCRIPT) {
                                DropdownMenuItem(
                                    text = { Text("📜 Ejecutar script externo", color = CyanAccent) },
                                    onClick = {
                                        expandedTaskMenu = false
                                        showScriptDialog = true
                                    }
                                )
                            }
                            DropdownMenuItem(
                                text = { Text("⏱️ Programar tarea", color = YellowAccent) },
                                onClick = {
                                    expandedTaskMenu = false
                                    showIntervalDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🛑 Detener programación", color = RedPrimary) },
                                onClick = {
                                    expandedTaskMenu = false
                                    vm.stopScheduledTask(task.id)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("⚙️ Ajustes", color = NeutralDark) },
                                onClick = {
                                    expandedTaskMenu = false
                                    selectedTask = task
                                    showEditDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🗑️ Borrar tarea", color = RedPrimary) },
                                onClick = {
                                    expandedTaskMenu = false
                                    selectedTask = task
                                    showDeleteDialog = true
                                }
                            )
                        }

                        if (showIntervalDialog)
                            DialogIntervaloTarea(vm = vm, task = task, onDismiss = { showIntervalDialog = false })

                        if (showBackupDialog)
                            DialogBackupFolder(vm = vm, task = task, onDismiss = { showBackupDialog = false })
                    }
                    if (showScriptDialog)
                        DialogRunScript(vm = vm, task = task, onDismiss = { showScriptDialog = false })

                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // 🧾 LOGS
        Text(
            "LOGS",
            color = NeutralDark,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFE9E9E9))
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (vm.logs.isEmpty()) {
                Text("No hay registros aún...", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    vm.logs.forEach { log ->
                        var expanded by remember { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = !expanded },
                            colors = CardDefaults.cardColors(
                                containerColor = if (log.isError) Color(0xFFFFE6E6) else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        log.message,
                                        color = if (log.isError) RedPrimary else NeutralDark,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                    )

                                    Text(
                                        log.timestamp,
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }

                                if (expanded && log.details.isNotBlank()) {
                                    Spacer(Modifier.height(6.dp))
                                    HorizontalDivider(color = Color(0xFFE0E0E0))
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        text = log.details.trim(),
                                        color = NeutralDark,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 💬 Diálogos generales
    if (showCreateDialog)
        DialogCrearTarea(vm, onDismiss = { showCreateDialog = false })

    if (showDeleteDialog)
        DialogBorrarTarea(vm, selectedTask, onDismiss = { showDeleteDialog = false })

    if (showEditDialog && selectedTask != null)
        DialogEditarTarea(vm, selectedTask, onDismiss = {
            showEditDialog = false
            selectedTask = null
        })

    if (showSelectDialog)
        DialogSeleccionarTarea(
            vm = vm,
            onDismiss = { showSelectDialog = false },
            onTaskSelected = { task ->
                selectedTask = task
                showSelectDialog = false
                showEditDialog = true
            }
        )

    if (showInfoDialog)
        DialogInfoApp(onDismiss = { showInfoDialog = false })

    if (showGuideDialog)
        DialogGuideApp(onDismiss = { showGuideDialog = false })
}

@Composable
fun DialogInfoApp(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = Color(0xFFBA3023))
            }
        },
        title = {
            Text(
                "Acerca de Automatizador de Tareas",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFFBA3023),
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text("👤 Desarrollado por: Iván Durán", color = Color(0xFF1C1C1C))
                Text("⚙️ Versión: 1.0.0", color = Color(0xFF1C1C1C))
                Text("📅 Fecha de lanzamiento: 2 de noviembre de 2025", color = Color(0xFF1C1C1C))
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text(
                    "© 2025 Iván Durán. Todos los derechos reservados.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        containerColor = Color(0xFFF5F5F5),
        tonalElevation = 8.dp
    )
}
@Composable
fun DialogRunScript(vm: TaskViewModel, task: TaskViewModel.TaskUi, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = RedPrimary, fontWeight = FontWeight.SemiBold)
            }
        },
        title = {
            Text(
                "Seleccionar script a ejecutar",
                color = RedPrimary,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    "Elige un archivo .bat (Windows) o .sh (Linux/Mac) para ejecutar:",
                    color = NeutralDark
                )
                Button(
                    onClick = {
                        val chooser = javax.swing.JFileChooser().apply {
                            fileSelectionMode = javax.swing.JFileChooser.FILES_ONLY
                            dialogTitle = "Selecciona un script"
                        }
                        val result = chooser.showOpenDialog(null)
                        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
                            val scriptPath = chooser.selectedFile.absolutePath
                            vm.runTaskNow(task.id, scriptPath)
                        }
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent)
                ) {
                    Text("Seleccionar script", color = Color.White)
                }
            }
        },
        containerColor = NeutralLight,
        tonalElevation = 8.dp
    )
}


@Composable
fun DialogGuideApp(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = RedPrimary, fontWeight = FontWeight.SemiBold)
            }
        },
        title = {
            Text(
                "Guía de uso",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = RedPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    "🧩 Automatizador de Tareas permite crear, programar y ejecutar tareas del sistema automáticamente.",
                    color = NeutralDark,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "• Usa el botón + para crear nuevas tareas y configurar su horario.",
                    color = NeutralDark,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "• Mantén pulsada una tarea para abrir su menú y programarla o editarla.",
                    color = NeutralDark,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "• El registro de eventos aparece en la parte inferior, dentro del apartado LOGS.",
                    color = NeutralDark,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "• Desde el menú lateral (⋮) puedes consultar esta guía, ver la información de la app o salir.",
                    color = NeutralDark,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        containerColor = NeutralLight,
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.medium
    )
}
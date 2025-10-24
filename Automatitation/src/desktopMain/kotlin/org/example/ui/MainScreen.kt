package org.example.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
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
            MainScreen()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(vm: TaskViewModel = remember { TaskViewModel() }) {

    // Estados del menú principal
    var expandedPlus by remember { mutableStateOf(false) }
    var expandedDots by remember { mutableStateOf(false) }

    // Estados para diálogos
    var showCreateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

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
            // ➕ Menú de acciones
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
                            showEditDialog = true
                        }
                    )
                }
            }

            // ⋮ Menú lateral derecho
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
                    DropdownMenuItem(text = { Text("GUÍA", color = NeutralDark) }, onClick = { expandedDots = false })
                    DropdownMenuItem(text = { Text("INFO", color = NeutralDark) }, onClick = { expandedDots = false })
                    DropdownMenuItem(
                        text = { Text("SALIR", color = OrangeBright) },
                        onClick = {
                            expandedDots = false
                            kotlin.system.exitProcess(0)
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // 🟥 Contenedor gris adaptativo con tareas
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
                    var expandedTaskMenu by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .background(RedPrimary)
                            .combinedClickable(
                                onClick = { expandedTaskMenu = true },
                                onLongClick = { expandedTaskMenu = true }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Texto centrado con color corporativo
                        Text(
                            text = task.name,
                            color = NeutralLight,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )

                        // 🔧 Menú contextual corporativo
                        DropdownMenu(
                            expanded = expandedTaskMenu,
                            onDismissRequest = { expandedTaskMenu = false },
                            modifier = Modifier.background(Color.White)
                        ) {
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
                    }
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
    }

    // 💬 Diálogos
    if (showCreateDialog)
        DialogCrearTarea(vm, onDismiss = { showCreateDialog = false })

    if (showDeleteDialog)
        DialogBorrarTarea(vm, selectedTask, onDismiss = { showDeleteDialog = false })

    if (showEditDialog)
        DialogEditarTarea(vm, selectedTask, onDismiss = { showEditDialog = false })
}

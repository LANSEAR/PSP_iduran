package org.example

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Automatizador de Tareas"
    ) {
        MaterialTheme {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(vm: TaskViewModel = remember { TaskViewModel() }) {
    var expandedPlus by remember { mutableStateOf(false) }      // menú del "+"
    var expandedDots by remember { mutableStateOf(false) }      // menú de los tres puntos

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // 🧩 Barra superior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // ➕ Botón "+"
            Box {
                Text(
                    "+",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.clickable { expandedPlus = true }
                )

                // Menú contextual del botón "+"
                DropdownMenu(
                    expanded = expandedPlus,
                    onDismissRequest = { expandedPlus = false },
                    modifier = Modifier.background(Color(0xFFE0E0E0))
                ) {
                    DropdownMenuItem(
                        text = { Text("CREAR TAREA", color = Color.Black) },
                        onClick = {
                            expandedPlus = false
                            vm.addTask("TAREA ${vm.tasks.size + 1}")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("BORRAR TAREA", color = Color.Black) },
                        onClick = {
                            expandedPlus = false
                            if (vm.tasks.isNotEmpty()) {
                                vm.removeTask(vm.tasks.last())
                            } else {
                                println("No hay tareas que borrar")
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("EDITAR TAREA", color = Color.Black) },
                        onClick = {
                            expandedPlus = false
                            println("Editar tarea general (a implementar)")
                        }
                    )
                }
            }

            // ⋮ Menú lateral derecho
            Box {
                Text(
                    "⋮",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.clickable { expandedDots = true }
                )

                DropdownMenu(
                    expanded = expandedDots,
                    onDismissRequest = { expandedDots = false },
                    modifier = Modifier.background(Color(0xFFE0E0E0))
                ) {
                    DropdownMenuItem(
                        text = { Text("GUÍA", color = Color.Black) },
                        onClick = {
                            expandedDots = false
                            println("Abrir guía de uso")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("INFO", color = Color.Black) },
                        onClick = {
                            expandedDots = false
                            println("Mostrar información del programa")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("SALIR", color = Color.Black) },
                        onClick = {
                            expandedDots = false
                            println("Saliendo de la aplicación…")
                            kotlin.system.exitProcess(0)
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // 🟥 Contenedor gris adaptativo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD9D9D9))
                .padding(16.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                vm.tasks.forEach { taskName ->
                    var expandedTaskMenu by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFFBA3023))
                            .combinedClickable(
                                onClick = { expandedTaskMenu = true },
                                onLongClick = { expandedTaskMenu = true }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            taskName,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        // Menú contextual individual de tarea
                        DropdownMenu(
                            expanded = expandedTaskMenu,
                            onDismissRequest = { expandedTaskMenu = false },
                            modifier = Modifier.background(Color(0xFFE0E0E0))
                        ) {
                            DropdownMenuItem(
                                text = { Text("Cambiar nombre", color = Color.Black) },
                                onClick = {
                                    expandedTaskMenu = false
                                    println("Cambiar nombre de $taskName")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cambiar función", color = Color.Black) },
                                onClick = {
                                    expandedTaskMenu = false
                                    println("Cambiar función/comando de $taskName")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cambiar horario", color = Color.Black) },
                                onClick = {
                                    expandedTaskMenu = false
                                    println("Cambiar horario de $taskName")
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
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

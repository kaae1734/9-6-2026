package com.kaae.appmovil.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.kaae.appmovil.logic.GestionTareas
import com.kaae.appmovil.model.EstadoTarea
import com.kaae.appmovil.model.Tarea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaScreen(gestionTareas: GestionTareas = remember { GestionTareas() }) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var filterState by remember { mutableStateOf<EstadoTarea?>(null) } // null means show all
    var sortAlphabetical by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }

    val tareasList = remember(refreshTrigger, filterState, sortAlphabetical) {
        var list = if (filterState == null) {
            gestionTareas.tareas
        } else {
            gestionTareas.filtrarPorEstado(filterState!!)
        }
        
        if (sortAlphabetical) {
            list = list.sortedBy { it.titulo }
        }
        list
    }
    
    val pendientesCount = remember(refreshTrigger) { gestionTareas.contarTareasPendientes() }
    val porcentajeCompletado = remember(refreshTrigger) { gestionTareas.calcularPorcentajeCompletado() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Gestión de Tareas", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título de la tarea") },
            modifier = Modifier.fillMaxWidth().testTag("titulo_input")
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth().testTag("descripcion_input")
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = {
                if (titulo.isNotBlank()) {
                    gestionTareas.agregarTarea(titulo, descripcion)
                    titulo = ""
                    descripcion = ""
                    refreshTrigger++
                }
            },
            modifier = Modifier.fillMaxWidth().testTag("agregar_button")
        ) {
            Text("Agregar Tarea")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Reto Adicional: Filtros y Orden
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilterChip(
                selected = filterState == null,
                onClick = { filterState = null },
                label = { Text("Todas") }
            )
            FilterChip(
                selected = filterState == EstadoTarea.PENDIENTE,
                onClick = { filterState = EstadoTarea.PENDIENTE },
                label = { Text("Pendientes") }
            )
            FilterChip(
                selected = filterState == EstadoTarea.COMPLETADA,
                onClick = { filterState = EstadoTarea.COMPLETADA },
                label = { Text("Completadas") }
            )
            FilterChip(
                selected = sortAlphabetical,
                onClick = { sortAlphabetical = !sortAlphabetical },
                label = { Text("A-Z") }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Pendientes: $pendientesCount",
                modifier = Modifier.testTag("pendientes_count")
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Completadas: ${"%.1f".format(porcentajeCompletado)}%")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            modifier = Modifier.weight(1f).testTag("tareas_list")
        ) {
            items(tareasList, key = { it.id }) { tarea ->
                TareaItem(
                    tarea = tarea,
                    onCompletar = {
                        gestionTareas.marcarComoCompletada(tarea.id)
                        refreshTrigger++
                    },
                    onEliminar = {
                        gestionTareas.eliminarTarea(tarea.id)
                        refreshTrigger++
                    }
                )
            }
        }
    }
}

@Composable
fun TareaItem(tarea: Tarea, onCompletar: () -> Unit, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = tarea.titulo, style = MaterialTheme.typography.titleMedium)
                if (tarea.descripcion.isNotBlank()) {
                    Text(text = tarea.descripcion, style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    text = tarea.estado.name,
                    color = if (tarea.estado == EstadoTarea.COMPLETADA) Color.Green else Color.Red,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            
            if (tarea.estado == EstadoTarea.PENDIENTE) {
                Checkbox(
                    checked = false,
                    onCheckedChange = { onCompletar() },
                    modifier = Modifier.testTag("completar_checkbox_${tarea.id}")
                )
            }
            
            IconButton(
                onClick = onEliminar,
                modifier = Modifier.testTag("eliminar_button_${tarea.id}")
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea")
            }
        }
    }
}

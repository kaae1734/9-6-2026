package com.kaae.appmovil.logic

import com.kaae.appmovil.model.EstadoTarea
import com.kaae.appmovil.model.Tarea

class GestionTareas {
    private val _tareas = mutableListOf<Tarea>()
    val tareas: List<Tarea> get() = _tareas

    fun agregarTarea(titulo: String, descripcion: String) {
        val id = if (_tareas.isEmpty()) 1 else _tareas.maxByOrNull { it.id }!!.id + 1
        _tareas.add(Tarea(id, titulo, descripcion))
    }

    fun eliminarTarea(id: Int) {
        _tareas.removeIf { it.id == id }
    }

    fun marcarComoCompletada(id: Int) {
        _tareas.find { it.id == id }?.let {
            it.estado = EstadoTarea.COMPLETADA
        }
    }

    fun obtenerTareasPendientes(): List<Tarea> {
        return _tareas.filter { it.estado == EstadoTarea.PENDIENTE }
    }

    fun contarTareasPendientes(): Int {
        return obtenerTareasPendientes().size
    }

    // Reto Adicional
    fun filtrarPorEstado(estado: EstadoTarea): List<Tarea> {
        return _tareas.filter { it.estado == estado }
    }

    fun ordenarAlfabeticamente(): List<Tarea> {
        return _tareas.sortedBy { it.titulo }
    }

    fun calcularPorcentajeCompletado(): Double {
        if (_tareas.isEmpty()) return 0.0
        val completadas = _tareas.count { it.estado == EstadoTarea.COMPLETADA }
        return (completadas.toDouble() / _tareas.size) * 100
    }
}

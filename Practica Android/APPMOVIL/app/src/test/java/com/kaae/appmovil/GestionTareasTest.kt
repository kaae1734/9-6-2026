package com.kaae.appmovil

import com.kaae.appmovil.logic.GestionTareas
import com.kaae.appmovil.model.EstadoTarea
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GestionTareasTest {

    private lateinit var gestion: GestionTareas

    @Before
    fun setUp() {
        gestion = GestionTareas()
    }

    @Test
    fun agregarTarea_incrementaLista() {
        gestion.agregarTarea("Test Tarea", "Descripción")
        assertEquals(1, gestion.tareas.size)
    }

    @Test
    fun eliminarTarea_desapareceDeLista() {
        gestion.agregarTarea("Tarea a eliminar", "")
        val id = gestion.tareas[0].id
        gestion.eliminarTarea(id)
        assertTrue(gestion.tareas.isEmpty())
    }

    @Test
    fun completarTarea_cambiaEstado() {
        gestion.agregarTarea("Tarea a completar", "")
        val id = gestion.tareas[0].id
        gestion.marcarComoCompletada(id)
        assertEquals(EstadoTarea.COMPLETADA, gestion.tareas[0].estado)
    }

    @Test
    fun contarTareasPendientes_retornaValorCorrecto() {
        gestion.agregarTarea("Tarea 1", "")
        gestion.agregarTarea("Tarea 2", "")
        gestion.marcarComoCompletada(gestion.tareas[0].id)
        assertEquals(1, gestion.contarTareasPendientes())
    }

    @Test
    fun listaVacia_retornaCeroPendientes() {
        assertEquals(0, gestion.contarTareasPendientes())
    }
}

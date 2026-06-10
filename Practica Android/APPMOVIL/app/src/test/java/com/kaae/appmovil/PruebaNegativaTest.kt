package com.kaae.appmovil

import com.kaae.appmovil.logic.GestionTareas
import org.junit.Assert.assertEquals
import org.junit.Test

class PruebaNegativaTest {

    @Test
    fun pruebaFallaIntencionalmente() {
        val gestion = GestionTareas()

        // Agregamos SOLO UNA tarea
        gestion.agregarTarea("Tarea Fallida", "Esta prueba DEBE fallar")

        // Forzamos el fallo: Esperamos 2 tareas cuando solo hay 1.
        // Asegúrate de que NO tenga "//" al principio de la línea de abajo.
        assertEquals("La cantidad de tareas no coincide (Falla intencional)", 2, gestion.tareas.size)
    }
}
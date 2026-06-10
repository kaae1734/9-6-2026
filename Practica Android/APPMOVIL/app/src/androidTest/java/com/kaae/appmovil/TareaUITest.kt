package com.kaae.appmovil

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.kaae.appmovil.ui.TareaScreen
import org.junit.Rule
import org.junit.Test

class TareaUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun agregarTarea_apareceEnPantalla() {
        composeTestRule.setContent {
            TareaScreen()
        }

        composeTestRule.onNodeWithTag("titulo_input").performTextInput("Nueva Tarea UI")
        composeTestRule.onNodeWithTag("agregar_button").performClick()

        composeTestRule.onNodeWithText("Nueva Tarea UI").assertIsDisplayed()
    }

    @Test
    fun botonAgregar_respondeAlClic() {
        composeTestRule.setContent {
            TareaScreen()
        }

        composeTestRule.onNodeWithTag("titulo_input").performTextInput("Tarea Click")
        composeTestRule.onNodeWithTag("agregar_button").performClick()

        // Si se agregó, el campo debería estar vacío ahora
        composeTestRule.onNodeWithTag("titulo_input").assertTextContains("")
    }

    @Test
    fun eliminarTarea_desapareceDeLista() {
        composeTestRule.setContent {
            TareaScreen()
        }

        // Agregar una tarea primero
        composeTestRule.onNodeWithTag("titulo_input").performTextInput("Tarea a borrar")
        composeTestRule.onNodeWithTag("agregar_button").performClick()
        
        // Asumiendo que es el primer ID (1)
        composeTestRule.onNodeWithTag("eliminar_button_1").performClick()

        composeTestRule.onNodeWithText("Tarea a borrar").assertDoesNotExist()
    }

    @Test
    fun mostrarPendientes_cantidadCorrecta() {
        composeTestRule.setContent {
            TareaScreen()
        }

        composeTestRule.onNodeWithTag("titulo_input").performTextInput("Pendiente 1")
        composeTestRule.onNodeWithTag("agregar_button").performClick()
        
        composeTestRule.onNodeWithTag("titulo_input").performTextInput("Pendiente 2")
        composeTestRule.onNodeWithTag("agregar_button").performClick()

        composeTestRule.onNodeWithTag("pendientes_count").assertTextContains("Pendientes: 2")
    }

    @Test
    fun campoEntrada_aceptaTexto() {
        composeTestRule.setContent {
            TareaScreen()
        }

        composeTestRule.onNodeWithTag("titulo_input").performTextInput("Hola Mundo")
        composeTestRule.onNodeWithTag("titulo_input").assertTextContains("Hola Mundo")
    }
}

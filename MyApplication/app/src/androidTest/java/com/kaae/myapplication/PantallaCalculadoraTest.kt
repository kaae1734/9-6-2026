package com.kaae.myapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class PantallaCalculadoraTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificarSumaEnInterfaz() {
        composeTestRule.setContent {
            PantallaCalculadora()
        }

        // Ingresar números usando testTags
        composeTestRule.onNodeWithTag("input1").performTextInput("10")
        composeTestRule.onNodeWithTag("input2").performTextInput("5")
        
        // Clic en el botón "Sumar"
        composeTestRule.onNodeWithText("Sumar").performClick()

        // Verificar resultado (10 + 5 = 15)
        composeTestRule.onNodeWithTag("resultado").assertTextEquals("15")
    }

    @Test
    fun verificarBotonAC() {
        composeTestRule.setContent {
            PantallaCalculadora()
        }

        // Llenar datos
        composeTestRule.onNodeWithTag("input1").performTextInput("10")
        composeTestRule.onNodeWithTag("input2").performTextInput("5")
        composeTestRule.onNodeWithText("Sumar").performClick()
        
        // Verificar que hay un resultado
        composeTestRule.onNodeWithTag("resultado").assertTextEquals("15")

        // Clic en AC (Limpiar)
        composeTestRule.onNodeWithText("AC").performClick()

        // Verificar que todo está reseteado
        composeTestRule.onNodeWithTag("input1").assertTextContains("")
        composeTestRule.onNodeWithTag("input2").assertTextContains("")
        composeTestRule.onNodeWithTag("resultado").assertTextEquals("0")
    }
}

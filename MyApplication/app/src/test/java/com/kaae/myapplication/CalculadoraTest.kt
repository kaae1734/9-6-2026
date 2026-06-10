package com.kaae.myapplication

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculadoraTest {

    private val calculadora = Calculadora()

    @Test
    fun verificarSuma() {
        assertEquals(8.0, calculadora.sumar(5.0, 3.0), 0.001)
    }

    @Test
    fun verificarResta() {
        assertEquals(2.0, calculadora.restar(5.0, 3.0), 0.001)
    }

    @Test
    fun verificarMultiplicacion() {
        assertEquals(15.0, calculadora.multiplicar(5.0, 3.0), 0.001)
    }

    @Test
    fun verificarDivision() {
        assertEquals(2.0, calculadora.dividir(10.0, 5.0), 0.001)
    }

    @Test
    fun verificarDivisionPorCero() {
        assertEquals(0.0, calculadora.dividir(10.0, 0.0), 0.001)
    }

    @Test
    fun verificarPorcentaje() {
        assertEquals(0.5, calculadora.porcentaje(50.0), 0.001)
    }
}

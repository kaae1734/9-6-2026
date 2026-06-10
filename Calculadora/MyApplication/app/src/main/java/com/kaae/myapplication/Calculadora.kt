package com.kaae.myapplication

class Calculadora {
    fun sumar(a: Double, b: Double): Double = a + b
    fun restar(a: Double, b: Double): Double = a - b
    fun multiplicar(a: Double, b: Double): Double = a * b
    fun dividir(a: Double, b: Double): Double = if (b != 0.0) a / b else 0.0
    fun porcentaje(a: Double): Double = a / 100.0
}

package com.kaae.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaae.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF0D0221)) {
                    PantallaCalculadora()
                }
            }
        }
    }
}

@Composable
fun PantallaCalculadora() {
    val calculadora = Calculadora()
    var display by remember { mutableStateOf("0") }
    var primerNumero by remember { mutableStateOf<Double?>(null) }
    var operacionActual by remember { mutableStateOf<String?>(null) }
    var esResultado by remember { mutableStateOf(false) }
    var historial by remember { mutableStateOf("") }

    fun formatResultado(valor: Double): String {
        return if (valor == valor.toLong().toDouble()) {
            valor.toLong().toString()
        } else {
            // Formatear decimales: máximo 6 decimales para evitar amontonamiento
            val formatted = "%.6f".format(valor).trimEnd('0').trimEnd('.')
            if (formatted.length > 12) formatted.take(12) else formatted
        }
    }

    fun realizarCalculo(n1: Double, n2: Double, op: String): Double {
        return when (op) {
            "+" -> calculadora.sumar(n1, n2)
            "−" -> calculadora.restar(n1, n2)
            "×" -> calculadora.multiplicar(n1, n2)
            "÷" -> calculadora.dividir(n1, n2)
            else -> n2
        }
    }

    fun onNumberClick(numero: String) {
        if (display == "0" || esResultado) {
            display = numero
        } else {
            // Limitar entrada de dígitos a 12 para mantener el diseño
            if (display.length < 12) display += numero
        }
        esResultado = false
        
        // Actualizar historial en tiempo real si hay una operación pendiente
        if (operacionActual != null && primerNumero != null) {
            historial = "${formatResultado(primerNumero!!)} $operacionActual $display"
        } else {
            historial = ""
        }
    }

    fun onOperatorClick(op: String) {
        val numeroActual = display.toDoubleOrNull() ?: 0.0
        
        if (primerNumero != null && operacionActual != null && !esResultado) {
            // Chaining: realizar operación previa antes de asignar la nueva
            val resultado = realizarCalculo(primerNumero!!, numeroActual, operacionActual!!)
            primerNumero = resultado
            display = formatResultado(resultado)
        } else {
            primerNumero = numeroActual
        }
        
        operacionActual = op
        esResultado = true
        historial = "${formatResultado(primerNumero!!)} $op"
    }

    fun onEqualClick() {
        val segundoNumero = display.toDoubleOrNull() ?: 0.0
        if (primerNumero != null && operacionActual != null) {
            val resultado = realizarCalculo(primerNumero!!, segundoNumero, operacionActual!!)
            historial = "${formatResultado(primerNumero!!)} $operacionActual ${formatResultado(segundoNumero)} ="
            display = formatResultado(resultado)
            primerNumero = null
            operacionActual = null
            esResultado = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0221))
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "NEON CALC", color = Color(0xFF00FFFF), fontSize = 20.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "::", color = Color(0xFFFB00FF), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Historial
        Text(
            text = historial,
            color = Color(0xFFFB00FF).copy(alpha = 0.7f),
            fontSize = if (historial.length > 15) 18.sp else 22.sp, // Tamaño dinámico para el historial
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            maxLines = 2,
            lineHeight = 26.sp
        )

        // Display con Tamaño Dinámico
        val dynamicFontSize = when {
            display.length > 10 -> 40.sp
            display.length > 8 -> 54.sp
            display.length > 5 -> 64.sp
            else -> 72.sp
        }

        Text(
            text = display,
            color = Color(0xFF00FFFF),
            fontSize = dynamicFontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .testTag("resultado"),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(16.dp))

        val botones = listOf(
            listOf("AC", "⌫", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "−"),
            listOf("1", "2", "3", "+"),
            listOf("⇳", "0", ".", "=")
        )

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            botones.forEach { fila ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    fila.forEach { texto ->
                        BotonNeon(
                            texto = texto,
                            modifier = Modifier.weight(1f).aspectRatio(1.2f),
                            onClick = {
                                when {
                                    texto == "AC" -> {
                                        display = "0"; primerNumero = null; operacionActual = null; esResultado = false; historial = ""
                                    }
                                    texto == "⌫" -> {
                                        if (!esResultado) {
                                            display = if (display.length > 1) display.dropLast(1) else "0"
                                            if (operacionActual != null && primerNumero != null) {
                                                historial = "${formatResultado(primerNumero!!)} $operacionActual $display"
                                            }
                                        }
                                    }
                                    texto == "%" -> {
                                        val actual = display.toDoubleOrNull() ?: 0.0
                                        val res = calculadora.porcentaje(actual)
                                        display = formatResultado(res)
                                        historial = "(${formatResultado(actual)})%"
                                        esResultado = true
                                    }
                                    texto == "." -> {
                                        if (esResultado) { display = "0."; esResultado = false }
                                        else if (!display.contains(".")) { display += "." }
                                    }
                                    texto.all { it.isDigit() } -> onNumberClick(texto)
                                    texto in listOf("÷", "×", "−", "+") -> onOperatorClick(texto)
                                    texto == "=" -> onEqualClick()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BotonNeon(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val neonCyan = Color(0xFF00FFFF); val neonMagenta = Color(0xFFFB00FF)
    val neonGreen = Color(0xFF39FF14); val neonYellow = Color(0xFFFFF01F)
    val color = when (texto) {
        "=" -> neonYellow
        "÷", "×", "−", "+" -> neonGreen
        "AC", "⌫", "%", "⇳" -> neonMagenta
        else -> neonCyan
    }
    Surface(
        onClick = onClick,
        modifier = modifier.shadow(if (texto == "=") 12.dp else 4.dp, shape = RoundedCornerShape(16.dp), ambientColor = color, spotColor = color),
        color = if (texto == "=") color.copy(alpha = 0.2f) else Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, color)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = texto, color = color, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
        }
    }
}

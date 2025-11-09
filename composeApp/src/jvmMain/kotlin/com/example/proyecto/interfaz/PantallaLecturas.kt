package com.example.proyecto.interfaz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.dominio.LecturaConsumo
import com.example.proyecto.persistencia.LecturaRepositorio
import com.example.proyecto.persistencia.MedidorRepositorio

/**
 * Pantalla lecturas
 *
 * @param medidorRepo
 * @param lecturaRepo
 */// Pantalla para registrar y consultar lecturas de consumo
@Composable
fun PantallaLecturas(
    medidorRepo: MedidorRepositorio,
    lecturaRepo: LecturaRepositorio
) {
    // Estado del formulario
    var codigoMedidor by remember { mutableStateOf("") }
    var anioTexto by remember { mutableStateOf("2025") }
    var mesTexto by remember { mutableStateOf("1") }
    var kwhTexto by remember { mutableStateOf("") }

    // Mensajes informativos y de error
    var mensaje by remember { mutableStateOf("") }

    // Lista de lecturas que se muestran en la parte inferior
    var lecturasMostradas by remember { mutableStateOf<List<LecturaConsumo>>(emptyList()) }

    // Limpia el formulario (manteniendo año y mes)
    fun limpiarFormulario() {
        kwhTexto = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Lecturas de consumo", style = MaterialTheme.typography.headlineSmall)

        // Formulario de registro y consulta de lecturas
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = codigoMedidor,
                onValueChange = { codigoMedidor = it },
                label = { Text("Código de medidor") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = anioTexto,
                    onValueChange = { anioTexto = it },
                    label = { Text("Año") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = mesTexto,
                    onValueChange = { mesTexto = it },
                    label = { Text("Mes") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = kwhTexto,
                onValueChange = { kwhTexto = it },
                label = { Text("kWh leídos") },
                modifier = Modifier.fillMaxWidth()
            )

            // Botones para registrar y consultar lecturas
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                Button(onClick = {
                    try {
                        val codigo = codigoMedidor.trim()
                        val anio = anioTexto.toIntOrNull()
                        val mes = mesTexto.toIntOrNull()
                        val kwh = kwhTexto.toDoubleOrNull()

                        if (codigo.isBlank()) {
                            mensaje = "Debes ingresar el código del medidor."
                            return@Button
                        }

                        val medidor = medidorRepo.listar().find { it.codigo == codigo }
                        if (medidor == null) {
                            mensaje = "No existe un medidor con código $codigo."
                            return@Button
                        }

                        if (anio == null) {
                            mensaje = "Año inválido."
                            return@Button
                        }
                        if (mes == null) {
                            mensaje = "Mes inválido."
                            return@Button
                        }

                        if (anio !in 2000..2025) {
                            mensaje = "El año debe estar entre 2000 y 2025."
                            return@Button
                        }

                        if (mes !in 1..12) {
                            mensaje = "El mes debe estar entre 1 y 12."
                            return@Button
                        }

                        if (kwh == null || kwh < 0.0) {
                            mensaje = "Los kWh leídos deben ser un número positivo."
                            return@Button
                        }

                        val lectura = LecturaConsumo(
                            idMedidor = codigo,
                            anio = anio,
                            mes = mes,
                            kwhLeidos = kwh
                        )

                        lecturaRepo.registrar(lectura)

                        lecturasMostradas = lecturaRepo.listaPorMedidorMes(
                            idMedidor = codigo,
                            anio = anio,
                            mes = mes
                        )

                        mensaje = "Lectura registrada correctamente."
                        limpiarFormulario()
                    } catch (e: Exception) {
                        mensaje = "Error al registrar lectura: ${e.message}"
                    }
                }) {
                    Text("Registrar lectura")
                }

                Button(onClick = {
                    try {
                        val codigo = codigoMedidor.trim()
                        val anio = anioTexto.toIntOrNull()
                        val mes = mesTexto.toIntOrNull()

                        if (codigo.isBlank()) {
                            mensaje = "Debes ingresar el código del medidor."
                            return@Button
                        }
                        if (anio == null || mes == null) {
                            mensaje = "Debes ingresar año y mes válidos."
                            return@Button
                        }

                        if (anio !in 2000..2025 || mes !in 1..12) {
                            mensaje = "Fecha inválida (año 2000–2025, mes 1–12)."
                            return@Button
                        }

                        lecturasMostradas = lecturaRepo.listaPorMedidorMes(
                            idMedidor = codigo,
                            anio = anio,
                            mes = mes
                        )

                        mensaje = if (lecturasMostradas.isEmpty()) {
                            "No hay lecturas registradas para ese medidor en $mes/$anio."
                        } else {
                            "Mostrando lecturas para $codigo en $mes/$anio."
                        }
                    } catch (e: Exception) {
                        mensaje = "Error al consultar lecturas: ${e.message}"
                    }
                }) {
                    Text("Ver lecturas del mes")
                }
            }
        }

        // Mensaje de estado
        if (mensaje.isNotEmpty()) {
            Text(mensaje, style = MaterialTheme.typography.bodyMedium)
        }

        Text("Lecturas registradas", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(lecturasMostradas) { lectura ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Medidor: ${lectura.idMedidor}")
                        Text("Año: ${lectura.anio}   Mes: ${lectura.mes}")
                        Text("kWh leídos: ${lectura.kwhLeidos}")
                    }
                }
            }
        }
    }
}

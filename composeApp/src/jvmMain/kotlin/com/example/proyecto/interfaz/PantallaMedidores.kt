package com.example.proyecto.interfaz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.dominio.Medidor
import com.example.proyecto.dominio.MedidorMonofasico
import com.example.proyecto.dominio.MedidorTrifasico
import com.example.proyecto.persistencia.MedidorRepositorio

/**
 * Pantalla medidores
 *
 * @param medidorRepo
 */// Pantalla para gestionar medidores (crear, listar y eliminar)
@Composable
fun PantallaMedidores(
    medidorRepo: MedidorRepositorio
) {
    // Estado del formulario
    var codigo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }

    var tipoMedidor by remember { mutableStateOf("MONO") } // MONO / TRI
    var potenciaTexto by remember { mutableStateOf("") }
    var factorPotenciaTexto by remember { mutableStateOf("") }

    // Mensajes informativos y de error
    var mensaje by remember { mutableStateOf("") }

    // Medidor seleccionado para edicion/eliminacion
    var medidorSeleccionado by remember { mutableStateOf<Medidor?>(null) }

    // Lista de medidores en memoria
    val medidores = remember { mutableStateListOf<Medidor>() }

    // Recarga la lista de medidores desde el repositorio
    fun recargarMedidores() {
        medidores.clear()
        medidores.addAll(medidorRepo.listar())
    }

    // Limpia los campos del formulario
    fun limpiarFormulario() {
        codigo = ""
        direccion = ""
        activo = true
        tipoMedidor = "MONO"
        potenciaTexto = ""
        factorPotenciaTexto = ""
        medidorSeleccionado = null
    }

    // Cargar medidores al entrar a la pantalla
    LaunchedEffect(Unit) {
        recargarMedidores()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Medidores CGE", style = MaterialTheme.typography.headlineSmall)

        // Formulario para crear/editar medidor
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                label = { Text("Código de medidor") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección de suministro") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(checked = activo, onCheckedChange = { activo = it })
                Text("Medidor activo")
            }

            // Seleccion del tipo de medidor
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Tipo de medidor:")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tipoMedidor == "MONO",
                        onClick = { tipoMedidor = "MONO" }
                    )
                    Text("Monofásico")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tipoMedidor == "TRI",
                        onClick = { tipoMedidor = "TRI" }
                    )
                    Text("Trifásico")
                }
            }

            OutlinedTextField(
                value = potenciaTexto,
                onValueChange = { potenciaTexto = it },
                label = { Text("Potencia máx (kW)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (tipoMedidor == "TRI") {
                OutlinedTextField(
                    value = factorPotenciaTexto,
                    onValueChange = { factorPotenciaTexto = it },
                    label = { Text("Factor de potencia") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Botones de crear, limpiar y eliminar
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                Button(onClick = {
                    try {
                        val codigoLimpio = codigo.trim()

                        if (codigoLimpio.isBlank()) {
                            mensaje = "Debes ingresar un código de medidor."
                            return@Button
                        }
                        if (direccion.isBlank()) {
                            mensaje = "Debes ingresar la dirección de suministro."
                            return@Button
                        }

                        val potencia = potenciaTexto.toDoubleOrNull()
                        if (potencia == null || potencia <= 0) {
                            mensaje = "Potencia máxima inválida."
                            return@Button
                        }

                        val factorPotencia =
                            if (tipoMedidor == "TRI") factorPotenciaTexto.toDoubleOrNull() else null
                        if (tipoMedidor == "TRI" && (factorPotencia == null || factorPotencia <= 0)) {
                            mensaje = "Factor de potencia inválido."
                            return@Button
                        }

                        val existentes = medidorRepo.listar()
                        if (existentes.any { it.codigo == codigoLimpio }) {
                            mensaje = "Ya existe un medidor con código $codigoLimpio."
                            return@Button
                        }

                        val nuevo: Medidor = if (tipoMedidor == "MONO") {
                            MedidorMonofasico(
                                id = codigoLimpio,
                                codigo = codigoLimpio,
                                direccionSuministro = direccion,
                                activo = activo,
                                potenciaMaxKw = potencia
                            )
                        } else {
                            MedidorTrifasico(
                                id = codigoLimpio,
                                codigo = codigoLimpio,
                                direccionSuministro = direccion,
                                activo = activo,
                                potenciaMaxKw = potencia,
                                factorPotencia = factorPotencia ?: 1.0
                            )
                        }

                        medidorRepo.crear(nuevo)
                        recargarMedidores()
                        limpiarFormulario()
                        mensaje = "Medidor creado correctamente."

                    } catch (e: Exception) {
                        mensaje = "Error al crear medidor: ${e.message}"
                    }
                }) {
                    Text("Crear medidor")
                }

                // Botón para limpiar formulario
                Button(onClick = {
                    limpiarFormulario()
                    mensaje = ""
                }) {
                    Text("Limpiar")
                }

                Button(
                    onClick = {
                        try {
                            val m = medidorSeleccionado
                            if (m == null) {
                                mensaje = "Selecciona un medidor para eliminar."
                                return@Button
                            }
                            val ok = medidorRepo.eliminar(m.codigo)
                            if (ok) {
                                recargarMedidores()
                                limpiarFormulario()
                                mensaje = "Medidor eliminado."
                            } else {
                                mensaje = "No se pudo eliminar el medidor."
                            }
                        } catch (e: Exception) {
                            mensaje = "Error al eliminar medidor: ${e.message}"
                        }
                    },
                    enabled = medidorSeleccionado != null
                ) {
                    Text("Eliminar")
                }
            }
        }

        // Mensaje de estado
        if (mensaje.isNotEmpty()) {
            Text(mensaje, style = MaterialTheme.typography.bodyMedium)
        }

        Text("Listado de medidores", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(medidores) { m ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            medidorSeleccionado = m
                            codigo = m.codigo
                            direccion = m.direccionSuministro
                            activo = m.activo

                            when (m) {
                                is MedidorMonofasico -> {
                                    tipoMedidor = "MONO"
                                    potenciaTexto = m.potenciaMaxKw.toString()
                                    factorPotenciaTexto = ""
                                }
                                is MedidorTrifasico -> {
                                    tipoMedidor = "TRI"
                                    potenciaTexto = m.potenciaMaxKw.toString()
                                    factorPotenciaTexto = m.factorPotencia.toString()
                                }
                                else -> {
                                    tipoMedidor = "MONO"
                                    potenciaTexto = ""
                                    factorPotenciaTexto = ""
                                }
                            }
                        }
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Código: ${m.codigo}")
                        Text("Dirección: ${m.direccionSuministro}")
                        Text("Activo: ${m.activo}")
                        when (m) {
                            is MedidorMonofasico -> {
                                Text("Tipo: Monofásico")
                                Text("Potencia máx: ${m.potenciaMaxKw} kW")
                            }
                            is MedidorTrifasico -> {
                                Text("Tipo: Trifásico")
                                Text("Potencia máx: ${m.potenciaMaxKw} kW")
                                Text("Factor de potencia: ${m.factorPotencia}")
                            }
                        }
                    }
                }
            }
        }
    }
}

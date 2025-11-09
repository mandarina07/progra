package com.example.proyecto.interfaz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyecto.dominio.Boleta
import com.example.proyecto.dominio.Cliente
import com.example.proyecto.persistencia.BoletaRepositorio
import com.example.proyecto.persistencia.ClienteRepositorio
import com.example.proyecto.servicios.BoletaService
import com.example.proyecto.servicios.PdfService
import java.io.File

/**
 * Pantalla boletas
 *
 * @param clienteRepo
 * @param boletaRepo
 * @param boletaService
 */// Pantalla principal para gestionar boletas (emitir, listar y exportar a PDF)
@Composable
fun PantallaBoletas(
    clienteRepo: ClienteRepositorio,
    boletaRepo: BoletaRepositorio,
    boletaService: BoletaService
) {
    // Estado de los campos de entrada
    var rutCliente by remember { mutableStateOf("") }
    var anioTexto by remember { mutableStateOf("2025") }
    var mesTexto by remember { mutableStateOf("11") }
    var tipoTarifa by remember { mutableStateOf("RESIDENCIAL") }

    // Estado de datos de negocio
    var boletasCliente by remember { mutableStateOf<List<Boleta>>(emptyList()) }
    var clienteActual by remember { mutableStateOf<Cliente?>(null) }
    var mensaje by remember { mutableStateOf("") }

    // Ruta del último PDF generado dentro del proyecto
    var ultimaRutaPdf by remember { mutableStateOf<String?>(null) }

    // Servicio para generar PDFs
    val pdfService = remember { PdfService() }

    // Genera el PDF de las boletas y lo guarda en Documentos/CGE y en el proyecto
    fun generarYGuardarPdf(cliente: Cliente, boletas: List<Boleta>, anio: Int, mes: Int): String {
        if (boletas.isEmpty()) return "No hay boletas para exportar."

        val mapaClientes = mapOf(cliente.rut to cliente)

        val bytes = pdfService.generarBoletasPDF(
            boletas = boletas,
            clientes = mapaClientes
        )
        if (bytes.isEmpty()) return "No se pudo generar el PDF."

        val nombreArchivo = "boletas_${cliente.rut}_${anio}_${mes}.pdf"

        // Carpeta del usuario: Documentos/CGE
        val home = System.getProperty("user.home")
        val carpetaUsuario = File(home, "Documentos/CGE")
        if (!carpetaUsuario.exists()) carpetaUsuario.mkdirs()
        val archivoUsuario = File(carpetaUsuario, nombreArchivo)
        archivoUsuario.writeBytes(bytes)

        // Carpeta interna del proyecto: composeApp/build/generated-pdfs
        val projectDir = System.getProperty("user.dir")
        val carpetaProyecto = File(projectDir, "composeApp/build/generated-pdfs")
        if (!carpetaProyecto.exists()) carpetaProyecto.mkdirs()
        val archivoProyecto = File(carpetaProyecto, nombreArchivo)
        archivoProyecto.writeBytes(bytes)

        // Guardar ruta para el botón "Ver PDF"
        ultimaRutaPdf = archivoProyecto.absolutePath

        return """
            PDF generado correctamente.
            - Carpeta del sistema: ${archivoUsuario.absolutePath}
            - Carpeta del proyecto: ${archivoProyecto.absolutePath}
        """.trimIndent()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Boletas CGE", style = MaterialTheme.typography.headlineSmall)

        // Seccion de datos de entrada (RUT, año, mes)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = rutCliente,
                onValueChange = { rutCliente = it },
                label = { Text("RUT cliente") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = anioTexto,
                onValueChange = { anioTexto = it },
                label = { Text("Año") },
                modifier = Modifier.width(100.dp)
            )
            OutlinedTextField(
                value = mesTexto,
                onValueChange = { mesTexto = it },
                label = { Text("Mes") },
                modifier = Modifier.width(80.dp)
            )
        }

        // Selector de tipo de tarifa
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tipo de tarifa:")

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipoTarifa == "RESIDENCIAL",
                    onClick = { tipoTarifa = "RESIDENCIAL" }
                )
                Text("Residencial")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipoTarifa == "COMERCIAL",
                    onClick = { tipoTarifa = "COMERCIAL" }
                )
                Text("Comercial")
            }
        }

        // Botones principales: emitir boleta, exportar PDF, ver PDF
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            Button(onClick = {
                try {
                    val anio = anioTexto.toIntOrNull()
                    val mes = mesTexto.toIntOrNull()

                    if (rutCliente.isBlank()) {
                        mensaje = "Debes ingresar el RUT del cliente."
                        return@Button
                    }
                    if (anio == null || anio <= 0) {
                        mensaje = "Año inválido."
                        return@Button
                    }
                    if (mes == null || mes !in 1..12) {
                        mensaje = "Mes inválido (1-12)."
                        return@Button
                    }

                    val cliente = clienteRepo.obtenerPorRut(rutCliente)
                    if (cliente == null) {
                        mensaje = "No se encontró cliente con RUT $rutCliente"
                        return@Button
                    }
                    clienteActual = cliente

                    val boleta = boletaService.emitirBoletaMensual(
                        rutCliente = rutCliente,
                        anio = anio,
                        mes = mes,
                        tipoTarifa = tipoTarifa
                    )

                    boletasCliente = boletaRepo.listaPorCliente(rutCliente)

                    val msgPdf = generarYGuardarPdf(cliente, boletasCliente, anio, mes)

                    mensaje = buildString {
                        append("Boleta emitida correctamente (kWh: ${boleta.kwhTotal}).")
                        appendLine()
                        append(msgPdf)
                    }
                } catch (e: Exception) {
                    mensaje = "Error al emitir boleta: ${e.message}"
                }
            }) {
                Text("Emitir boleta")
            }

            Button(onClick = {
                try {
                    val cliente = clienteActual
                    if (cliente == null) {
                        mensaje = "Primero emite o selecciona boletas para un cliente."
                        return@Button
                    }
                    if (boletasCliente.isEmpty()) {
                        mensaje = "No hay boletas para exportar."
                        return@Button
                    }

                    val anio = anioTexto.toIntOrNull() ?: 0
                    val mes = mesTexto.toIntOrNull() ?: 0

                    mensaje = generarYGuardarPdf(cliente, boletasCliente, anio, mes)
                } catch (e: Exception) {
                    mensaje = "Error al generar PDF: ${e.message}"
                }
            }) {
                Text("Exportar PDF")
            }

            Button(
                onClick = {
                    try {
                        val ruta = ultimaRutaPdf
                        if (ruta == null) {
                            mensaje = "Aún no se ha generado ningún PDF en esta sesión."
                            return@Button
                        }
                        val archivo = File(ruta)
                        if (!archivo.exists()) {
                            mensaje = "El PDF ya no existe en la ruta guardada."
                            return@Button
                        }
                        java.awt.Desktop.getDesktop().open(archivo)
                    } catch (e: Exception) {
                        mensaje = "No se pudo abrir el PDF: ${e.message}"
                    }
                },
                enabled = ultimaRutaPdf != null
            ) {
                Text("Ver PDF")
            }
        }

        if (mensaje.isNotEmpty()) {
            Text(mensaje, style = MaterialTheme.typography.bodyMedium)
        }

        // Lista de boletas del cliente
        Text("Boletas del cliente", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(boletasCliente) { b ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Año ${b.anio}, Mes ${b.mes}")
                        Text("kWh total: ${b.kwhTotal}")
                        Text("Subtotal: ${b.detalle.subtotal}")
                        Text("Cargos: ${b.detalle.cargos}")
                        Text("IVA: ${b.detalle.iva}")
                        Text("Total: ${b.detalle.total}")
                        Text("Estado: ${b.estado}")
                    }
                }
            }
        }
    }
}

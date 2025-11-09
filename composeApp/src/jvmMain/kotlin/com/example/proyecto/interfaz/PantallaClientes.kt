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
import com.example.proyecto.dominio.Cliente
import com.example.proyecto.dominio.EstadoCliente
import com.example.proyecto.persistencia.ClienteRepositorio
import com.example.proyecto.validarEmail
import com.example.proyecto.validarRut

/**
 * Pantalla clientes
 *
 * @param clienteRepo
 */// Pantalla para gestionar los clientes (crear, editar, eliminar y buscar)
@Composable
fun PantallaClientes(
    clienteRepo: ClienteRepositorio
) {
    // Estado del formulario
    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }

    // Mensajes de validacion o informacion
    var mensaje by remember { mutableStateOf("") }

    // Cliente seleccionado para edicion/eliminacion
    var clienteSeleccionado by remember { mutableStateOf<Cliente?>(null) }

    // Lista de clientes en memoria
    val clientes = remember { mutableStateListOf<Cliente>() }

    // Texto para el filtro de busqueda
    var filtro by remember { mutableStateOf("") }

    // Limpia los campos del formulario
    fun limpiarFormulario() {
        rut = ""
        nombre = ""
        email = ""
        direccion = ""
        activo = true
        clienteSeleccionado = null
    }

    // Recarga la lista de clientes desde el repositorio
    fun recargarClientes() {
        clientes.clear()
        clientes.addAll(clienteRepo.listar())
    }

    // Cargar clientes al iniciar la pantalla
    LaunchedEffect(Unit) {
        recargarClientes()
    }

    // Layout principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Clientes CGE", style = MaterialTheme.typography.headlineSmall)

        // Formulario de creacion/edicion de cliente
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("RUT") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección de facturación") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = activo,
                    onCheckedChange = { activo = it }
                )
                Text("Cliente activo")
            }

            // Botones de crear/actualizar, limpiar y eliminar
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                // Botón crear/actualizar cliente
                Button(onClick = {
                    try {
                        if (rut.isBlank() || nombre.isBlank()) {
                            mensaje = "RUT y Nombre son obligatorios."
                            return@Button
                        }

                        if (!validarRut(rut)) {
                            mensaje = "RUT inválido o con formato incorrecto."
                            return@Button
                        }

                        if (email.isNotBlank() && !validarEmail(email)) {
                            mensaje =
                                "Email inválido. Usa un correo @gmail.com o @alumnos.ucm.cl."
                            return@Button
                        }

                        val estado = if (activo) EstadoCliente.ACTIVO else EstadoCliente.INACTIVO

                        if (clienteSeleccionado == null) {
                            val nuevo = Cliente(
                                id = rut,
                                rut = rut,
                                nombre = nombre,
                                email = email,
                                direccionFacturacion = direccion,
                                estado = estado
                            )
                            clienteRepo.crear(nuevo)
                            recargarClientes()
                            mensaje = "Cliente creado correctamente."
                        } else {
                            val original = clienteSeleccionado!!
                            val actualizado = Cliente(
                                id = original.id,
                                createdAt = original.createdAt,
                                updatedAt = original.updatedAt,
                                rut = rut,
                                nombre = nombre,
                                email = email,
                                direccionFacturacion = direccion,
                                estado = estado
                            )
                            clienteRepo.actualizar(actualizado)
                            recargarClientes()
                            mensaje = "Cliente actualizado correctamente."
                        }

                        limpiarFormulario()
                    } catch (e: Exception) {
                        mensaje = "Error al guardar cliente: ${e.message}"
                    }
                }) {
                    Text(if (clienteSeleccionado == null) "Crear" else "Actualizar")
                }

                Button(
                    onClick = {
                        limpiarFormulario()
                        mensaje = ""
                    }
                ) {
                    Text("Limpiar")
                }

                Button(
                    onClick = {
                        try {
                            val c = clienteSeleccionado
                            if (c == null) {
                                mensaje = "Selecciona un cliente para eliminar."
                                return@Button
                            }
                            val ok = clienteRepo.eliminar(c.rut)
                            if (ok) {
                                recargarClientes()
                                limpiarFormulario()
                                mensaje = "Cliente eliminado."
                            } else {
                                mensaje = "No se pudo eliminar el cliente."
                            }
                        } catch (e: Exception) {
                            mensaje = "Error al eliminar cliente: ${e.message}"
                        }
                    },
                    enabled = clienteSeleccionado != null
                ) {
                    Text("Eliminar")
                }
            }
        }

        // Mensajes de estado
        if (mensaje.isNotEmpty()) {
            Text(mensaje, style = MaterialTheme.typography.bodyMedium)
        }

        Text("Listado de clientes", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = filtro,
            onValueChange = { filtro = it },
            label = { Text("Buscar por RUT o nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        // Filtrado de clientes segun el texto de busqueda
        val clientesFiltrados = if (filtro.isBlank()) {
            clientes
        } else {
            clientes.filter {
                it.rut.contains(filtro, ignoreCase = true) ||
                        it.nombre.contains(filtro, ignoreCase = true)
            }
        }

        // Lista de clientes
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(clientesFiltrados) { c ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            clienteSeleccionado = c
                            rut = c.rut
                            nombre = c.nombre
                            email = c.email
                            direccion = c.direccionFacturacion
                            activo = c.estado == EstadoCliente.ACTIVO
                        }
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("${c.nombre} (${c.rut})")
                        Text("Email: ${c.email}")
                        Text("Dirección: ${c.direccionFacturacion}")
                        Text("Estado: ${c.estado}")
                    }
                }
            }
        }
    }
}

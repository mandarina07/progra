package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Boleta
import com.example.proyecto.dominio.Cliente
import com.example.proyecto.dominio.EstadoBoleta
import com.example.proyecto.dominio.EstadoCliente
import com.example.proyecto.dominio.LecturaConsumo
import com.example.proyecto.dominio.Medidor
import com.example.proyecto.dominio.MedidorMonofasico
import com.example.proyecto.dominio.MedidorTrifasico
import com.example.proyecto.dominio.TarifaDetalle

class PersistenciaDatos(
    private val storage: StorageDriver
) {

    private val clientes = mutableListOf<Cliente>()
    private val medidores = mutableListOf<Medidor>()
    private val lecturas = mutableListOf<LecturaConsumo>()
    private val boletas = mutableListOf<Boleta>()

    private val KEY_CLIENTES = "clientes"
    private val KEY_MEDIDORES = "medidores"
    private val KEY_LECTURAS = "lecturas"
    private val KEY_BOLETAS = "boletas"

    init {
        cargarClientesDesdeDisco()
        cargarMedidoresDesdeDisco()
        cargarLecturasDesdeDisco()
        cargarBoletasDesdeDisco()
    }

    // ===================== CLIENTES =====================

    fun agregarCliente(cliente: Cliente) {
        clientes.add(cliente)
        guardarClientesEnDisco()
    }

    fun actualizarCliente(cliente: Cliente) {
        val index = clientes.indexOfFirst { it.rut == cliente.rut }
        if (index >= 0) {
            clientes[index] = cliente
            guardarClientesEnDisco()
        }
    }

    fun eliminarClientePorRut(rut: String): Boolean {
        val removed = clientes.removeAll { it.rut == rut }
        if (removed) guardarClientesEnDisco()
        return removed
    }

    fun obtenerClientePorRut(rut: String): Cliente? =
        clientes.firstOrNull { it.rut == rut }

    fun listarClientes(): List<Cliente> = clientes.toList()

    private fun guardarClientesEnDisco() {
        // id;rut;nombre;email;direccion;estado;createdAt;updatedAt
        val texto = clientes.joinToString("\n") { c ->
            listOf(
                c.id,
                c.rut,
                c.nombre,
                c.email,
                c.direccionFacturacion,
                c.estado.name,
                c.createdAt,
                c.updatedAt
            ).joinToString(";")
        }
        // 👇 multiplatform
        storage.put(KEY_CLIENTES, texto.encodeToByteArray())
    }

    private fun cargarClientesDesdeDisco() {
        clientes.clear()
        val bytes = storage.get(KEY_CLIENTES) ?: return
        val texto = bytes.decodeToString()   // 👈 multiplatform

        texto.lineSequence()
            .filter { it.isNotBlank() }
            .forEach { linea ->
                val p = linea.split(";")
                if (p.size >= 8) {
                    val cliente = Cliente(
                        id = p[0],
                        rut = p[1],
                        nombre = p[2],
                        email = p[3],
                        direccionFacturacion = p[4],
                        estado = runCatching { EstadoCliente.valueOf(p[5]) }
                            .getOrElse { EstadoCliente.ACTIVO },
                        createdAt = p[6],
                        updatedAt = p[7]
                    )
                    clientes.add(cliente)
                }
            }
    }

    // ===================== MEDIDORES =====================

    fun agregarMedidor(medidor: Medidor) {
        medidores.add(medidor)
        guardarMedidoresEnDisco()
    }

    fun listarMedidores(): List<Medidor> = medidores.toList()

    fun obtenerMedidoresPorRut(rutCliente: String): List<Medidor> {
        // TODO: relación Cliente–Medidor
        return emptyList()
    }

    fun eliminarMedidorPorCodigo(codigo: String): Boolean {
        val removed = medidores.removeAll { it.codigo == codigo }
        if (removed) guardarMedidoresEnDisco()
        return removed
    }

    private fun guardarMedidoresEnDisco() {
        val texto = medidores.joinToString("\n") { m ->
            when (m) {
                is MedidorMonofasico -> listOf(
                    "M",
                    m.id,
                    m.createdAt,
                    m.updatedAt,
                    m.codigo,
                    m.direccionSuministro,
                    m.activo.toString(),
                    m.potenciaMaxKw.toString()
                ).joinToString(";")

                is MedidorTrifasico -> listOf(
                    "T",
                    m.id,
                    m.createdAt,
                    m.updatedAt,
                    m.codigo,
                    m.direccionSuministro,
                    m.activo.toString(),
                    m.potenciaMaxKw.toString(),
                    m.factorPotencia.toString()
                ).joinToString(";")

                else -> ""
            }
        }
        storage.put(KEY_MEDIDORES, texto.encodeToByteArray())
    }

    private fun cargarMedidoresDesdeDisco() {
        medidores.clear()
        val bytes = storage.get(KEY_MEDIDORES) ?: return
        val texto = bytes.decodeToString()

        texto.lineSequence()
            .filter { it.isNotBlank() }
            .forEach { linea ->
                val p = linea.split(";")
                if (p.isEmpty()) return@forEach
                when (p[0]) {
                    "M" -> if (p.size >= 8) {
                        medidores.add(
                            MedidorMonofasico(
                                id = p[1],
                                createdAt = p[2],
                                updatedAt = p[3],
                                codigo = p[4],
                                direccionSuministro = p[5],
                                activo = p[6].toBoolean(),
                                potenciaMaxKw = p[7].toDoubleOrNull() ?: 0.0
                            )
                        )
                    }

                    "T" -> if (p.size >= 9) {
                        medidores.add(
                            MedidorTrifasico(
                                id = p[1],
                                createdAt = p[2],
                                updatedAt = p[3],
                                codigo = p[4],
                                direccionSuministro = p[5],
                                activo = p[6].toBoolean(),
                                potenciaMaxKw = p[7].toDoubleOrNull() ?: 0.0,
                                factorPotencia = p[8].toDoubleOrNull() ?: 0.0
                            )
                        )
                    }
                }
            }
    }

    // ===================== LECTURAS =====================

    fun registrarLectura(lectura: LecturaConsumo) {
        lecturas.add(lectura)
        guardarLecturasEnDisco()
    }

    fun lecturasPorMedidorYMes(idMedidor: String, anio: Int, mes: Int): List<LecturaConsumo> =
        lecturas.filter { it.idMedidor == idMedidor && it.anio == anio && it.mes == mes }

    fun ultimaLectura(idMedidor: String): LecturaConsumo? =
        lecturas
            .filter { it.idMedidor == idMedidor }
            .maxByOrNull { it.anio * 100 + it.mes }

    private fun guardarLecturasEnDisco() {
        val texto = lecturas.joinToString("\n") { l ->
            listOf(
                l.idMedidor,
                l.anio.toString(),
                l.mes.toString(),
                l.kwhLeidos.toString()
            ).joinToString(";")
        }
        storage.put(KEY_LECTURAS, texto.encodeToByteArray())
    }

    private fun cargarLecturasDesdeDisco() {
        lecturas.clear()
        val bytes = storage.get(KEY_LECTURAS) ?: return
        val texto = bytes.decodeToString()

        texto.lineSequence()
            .filter { it.isNotBlank() }
            .forEach { linea ->
                val p = linea.split(";")
                if (p.size >= 4) {
                    lecturas.add(
                        LecturaConsumo(
                            idMedidor = p[0],
                            anio = p[1].toIntOrNull() ?: 0,
                            mes = p[2].toIntOrNull() ?: 1,
                            kwhLeidos = p[3].toDoubleOrNull() ?: 0.0
                        )
                    )
                }
            }
    }

    // ===================== BOLETAS =====================

    fun guardarBoleta(boleta: Boleta) {
        boletas.add(boleta)
        guardarBoletasEnDisco()
    }

    fun obtenerBoleta(rutCliente: String, anio: Int, mes: Int): Boleta? =
        boletas.firstOrNull { it.idCliente == rutCliente && it.anio == anio && it.mes == mes }

    fun listaBoletasPorCliente(rutCliente: String): List<Boleta> =
        boletas.filter { it.idCliente == rutCliente }

    private fun guardarBoletasEnDisco() {
        val texto = boletas.joinToString("\n") { b ->
            listOf(
                b.idCliente,
                b.anio.toString(),
                b.mes.toString(),
                b.kwhTotal.toString(),
                b.detalle.subtotal.toString(),
                b.detalle.cargos.toString(),
                b.detalle.iva.toString(),
                b.detalle.total.toString(),
                b.estado.name
            ).joinToString(";")
        }
        storage.put(KEY_BOLETAS, texto.encodeToByteArray())
    }

    private fun cargarBoletasDesdeDisco() {
        boletas.clear()
        val bytes = storage.get(KEY_BOLETAS) ?: return
        val texto = bytes.decodeToString()

        texto.lineSequence()
            .filter { it.isNotBlank() }
            .forEach { linea ->
                val p = linea.split(";")
                if (p.size >= 9) {
                    val detalle = TarifaDetalle(
                        kwh = p[3].toDoubleOrNull() ?: 0.0,
                        subtotal = p[4].toDoubleOrNull() ?: 0.0,
                        cargos = p[5].toDoubleOrNull() ?: 0.0,
                        iva = p[6].toDoubleOrNull() ?: 0.0,
                        total = p[7].toDoubleOrNull() ?: 0.0
                    )
                    val boleta = Boleta(
                        idCliente = p[0],
                        anio = p[1].toIntOrNull() ?: 0,
                        mes = p[2].toIntOrNull() ?: 1,
                        kwhTotal = detalle.kwh,
                        detalle = detalle,
                        estado = runCatching { EstadoBoleta.valueOf(p[8]) }
                            .getOrElse { EstadoBoleta.EMITIDA }
                    )
                    boletas.add(boleta)
                }
            }
    }
}

package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Boleta
import com.example.proyecto.dominio.Cliente
import com.example.proyecto.dominio.LecturaConsumo
import com.example.proyecto.dominio.Medidor

/*
* Capa de persistencia centralizada
*  Por ahora guarda todo en memoria usando colecciones.
* MIRAR BIEN PORQUE PUEDE TENER CAMBIOS
* */

class PersistenciaDatos {

    private val clientes = mutableListOf<Cliente>()
    private val medidores = mutableListOf<Medidor>()
    private val lecturas = mutableListOf<LecturaConsumo>()
    private val boletas = mutableListOf<Boleta>()

    // ----- CLIENTES -----
    fun agregarCliente(cliente: Cliente) {
        clientes.add(cliente)
    }

    fun actualizarCliente(cliente: Cliente) {
        val index = clientes.indexOfFirst { it.rut == cliente.rut }
        if (index >= 0) {
            clientes[index] = cliente
        }
    }

    fun eliminarClientePorRut(rut: String): Boolean {
        return clientes.removeIf { it.rut == rut }
    }

    fun obtenerClientePorRut(rut: String): Cliente? =
        clientes.firstOrNull { it.rut == rut }

    fun listarClientes(): List<Cliente> = clientes.toList()

    // ----- MEDIDORES -----
    fun agregarMedidor(medidor: Medidor) {
        medidores.add(medidor)
    }

    fun listarMedidores(): List<Medidor> = medidores.toList()

    /**
     * Aquí la implementación real depende de cómo relaciones Medidor con Cliente.
     * Por ahora lo dejamos como TODO para que compile y podamos seguir.
     */
    fun obtenerMedidoresPorRut(rutCliente: String): List<Medidor> {
        // TODO: implementar cuando definas cómo se asocian medidores a clientes en tu dominio
        return emptyList()
    }

    fun eliminarMedidorPorCodigo(codigo: String): Boolean =
        medidores.removeIf { it.codigo == codigo }

    // ----- LECTURAS -----
    fun registrarLectura(lectura: LecturaConsumo) {
        lecturas.add(lectura)
    }

    fun lecturasPorMedidorYMes(idMedidor: String, anio: Int, mes: Int): List<LecturaConsumo> =
        lecturas.filter { it.idMedidor == idMedidor && it.anio == anio && it.mes == mes }

    fun ultimaLectura(idMedidor: String): LecturaConsumo? =
        lecturas
            .filter { it.idMedidor == idMedidor }
            .maxByOrNull { it.anio * 100 + it.mes }  // año-mes más reciente

    // ----- BOLETAS -----
    fun guardarBoleta(boleta: Boleta) {
        boletas.add(boleta)
    }

    fun obtenerBoleta(rutCliente: String, anio: Int, mes: Int): Boleta? =
        boletas.firstOrNull { it.idCliente == rutCliente && it.anio == anio && it.mes == mes }

    fun listaBoletasPorCliente(rutCliente: String): List<Boleta> =
        boletas.filter { it.idCliente == rutCliente }
}

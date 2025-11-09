package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Boleta

/**
 * Boleta repositorio impl
 *
 * @property persistenciaDatos
 * @constructor Create empty Boleta repositorio impl
 */
class BoletaRepositorioImpl(
    private val persistenciaDatos: PersistenciaDatos
) : BoletaRepositorio {

    override fun guardar(boleta: Boleta) {
        persistenciaDatos.guardarBoleta(boleta)
    }

    override fun obtener(rutCliente: String, anio: Int, mes: Int): Boleta? =
        persistenciaDatos.obtenerBoleta(rutCliente, anio, mes)

    override fun listaPorCliente(rutCliente: String): List<Boleta> =
        persistenciaDatos.listaBoletasPorCliente(rutCliente)
}
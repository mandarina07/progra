package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Medidor

/**
 * Medidor repositorio impl
 *
 * @property persistenciaDatos
 * @constructor Create empty Medidor repositorio impl
 */
class MedidorRepositorioImpl(
    private val persistenciaDatos: PersistenciaDatos
) : MedidorRepositorio {

    override fun crear(medidor: Medidor) {
        persistenciaDatos.agregarMedidor(medidor)
    }

    override fun listar(): List<Medidor> =
        persistenciaDatos.listarMedidores()

    override fun obtenerPorRut(rutCliente: String): List<Medidor> =
        persistenciaDatos.obtenerMedidoresPorRut(rutCliente)

    override fun eliminar(codigo: String): Boolean =
        persistenciaDatos.eliminarMedidorPorCodigo(codigo)
}
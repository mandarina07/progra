package com.example.proyecto.persistencia

import com.example.proyecto.dominio.LecturaConsumo

/**
 * Lectura repositorio impl
 *
 * @property persistenciaDatos
 * @constructor Create empty Lectura repositorio impl
 */
class LecturaRepositorioImpl(
private val persistenciaDatos: PersistenciaDatos
) : LecturaRepositorio {

    override fun registrar(lectura: LecturaConsumo) {
        persistenciaDatos.registrarLectura(lectura)
    }

    override fun listaPorMedidorMes(idMedidor: String, anio: Int, mes: Int): List<LecturaConsumo> =
        persistenciaDatos.lecturasPorMedidorYMes(idMedidor, anio, mes)

    override fun ultimaLectura(idMedidor: String): LecturaConsumo? =
        persistenciaDatos.ultimaLectura(idMedidor)
}

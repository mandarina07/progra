package com.example.proyecto.persistencia

import com.example.proyecto.dominio.LecturaConsumo

/**
 * Lectura repositorio
 *
 * @constructor Create empty Lectura repositorio
 */
interface LecturaRepositorio {
    /**
     * Registrar
     *
     * @param lectura
     */
    fun registrar(lectura: LecturaConsumo)

    /**
     * Lista por medidor mes
     *
     * @param idMedidor
     * @param anio
     * @param mes
     * @return
     */
    fun listaPorMedidorMes(idMedidor: String, anio: Int, mes: Int): List<LecturaConsumo>

    /**
     * Ultima lectura
     *
     * @param idMedidor
     * @return
     */
    fun ultimaLectura(idMedidor: String): LecturaConsumo?
}
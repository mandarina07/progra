package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Boleta

/**
 * Boleta repositorio
 *
 * @constructor Create empty Boleta repositorio
 */
interface BoletaRepositorio {
    /**
     * Guardar
     *
     * @param boleta
     */
    fun guardar(boleta: Boleta)

    /**
     * Obtener
     *
     * @param rutCliente
     * @param anio
     * @param mes
     * @return
     */
    fun obtener(rutCliente: String, anio: Int, mes: Int): Boleta?

    /**
     * Lista por cliente
     *
     * @param rutCliente
     * @return
     */
    fun listaPorCliente(rutCliente: String): List<Boleta>
}
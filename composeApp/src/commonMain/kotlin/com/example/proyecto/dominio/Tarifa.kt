package com.example.proyecto.dominio

/*
* Interface comun
* Aplica polimorfismo
* */

/**
 * Tarifa
 *
 * @constructor Create empty Tarifa
 */
interface Tarifa {
    /**
     * Nombre
     *
     * @return
     */
    fun nombre(): String

    /**
     * Calcular
     *
     * @param kwh
     * @return
     */
    fun calcular(kwh: Double): TarifaDetalle

}
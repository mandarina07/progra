package com.example.proyecto.dominio

/*
* Medidor de consumo
* Abstracta por los sub(monofasico/trifasico)
* */

abstract class Medidor(
    id: String,
    createdAt: String = "",
    updatedAt: String = "",
    val codigo: String,
    var direccionSuministro: String,
    var activo: Boolean = true,
) : EntidadBase(id, createdAt, updatedAt) {

    /**
     * Devuelve un texto que indica el tipo de medidor.
     * Lo implementan las subclases.
     */
    abstract fun tipo(): String

}
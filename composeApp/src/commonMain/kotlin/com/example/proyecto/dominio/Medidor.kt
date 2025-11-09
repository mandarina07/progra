package com.example.proyecto.dominio

/*
* Medidor de consumo
* Abstracta por los sub(monofasico/trifasico)
* */

/**
 * Medidor
 *
 * @property codigo
 * @property direccionSuministro
 * @property activo
 * @constructor
 *
 * @param id
 * @param createdAt
 * @param updatedAt
 */
abstract class Medidor(
    id: String,
    createdAt: String = "",
    updatedAt: String = "",
    val codigo: String,
    var direccionSuministro: String,
    var activo: Boolean = true
) : EntidadBase(id, createdAt, updatedAt) {

    /**
     * Tipo
     *
     * @return
     */// Método que cada subclase debe implementar para indicar su tipo
    abstract fun tipo(): String
}

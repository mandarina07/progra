package com.example.proyecto.dominio

/**
 * Medidor trifasico
 *
 * @property potenciaMaxKw
 * @property factorPotencia
 * @constructor
 *
 * @param id
 * @param createdAt
 * @param updatedAt
 * @param codigo
 * @param direccionSuministro
 * @param activo
 */
class MedidorTrifasico (
    id: String,
    createdAt: String = "",
    updatedAt: String = "",
    codigo: String,
    direccionSuministro: String,
    activo: Boolean = true,
    var potenciaMaxKw: Double,
    var factorPotencia: Double
) : Medidor(id, createdAt, updatedAt, codigo, direccionSuministro, activo) {

    override fun tipo(): String = "Trifasico"
}
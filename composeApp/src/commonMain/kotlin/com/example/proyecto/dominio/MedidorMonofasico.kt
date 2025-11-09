package com.example.proyecto.dominio

/**
 * Medidor monofasico
 *
 * @property potenciaMaxKw
 * @constructor
 *
 * @param id
 * @param createdAt
 * @param updatedAt
 * @param codigo
 * @param direccionSuministro
 * @param activo
 */
class MedidorMonofasico (
    id: String,
    createdAt: String = "",
    updatedAt: String = "",
    codigo: String,
    direccionSuministro: String,
    activo: Boolean = true,
    var potenciaMaxKw: Double

) : Medidor(id, createdAt, updatedAt, codigo, direccionSuministro, activo) {

    override fun tipo(): String = "Monofasico"

}
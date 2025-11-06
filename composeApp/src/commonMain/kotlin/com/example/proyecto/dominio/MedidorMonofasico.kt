package com.example.proyecto.dominio

/*
* Medidor monofasico
* */

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
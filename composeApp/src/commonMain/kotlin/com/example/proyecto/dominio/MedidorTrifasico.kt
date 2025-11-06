package com.example.proyecto.dominio

/*
* Medidor Trifasico
* */

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
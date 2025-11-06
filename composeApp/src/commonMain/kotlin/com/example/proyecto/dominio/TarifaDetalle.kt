package com.example.proyecto.dominio

/*
* Resultado calculo
* */

data class TarifaDetalle (
    val kwh: Double,
    val subtotal: Double,
    val cargos: Double,
    val iva: Double,
    val total: Double
){

}
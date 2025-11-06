package com.example.proyecto.dominio

/*
* Boleta mensual
* */

class Boleta (
    val idCliente: String,
    val anio: Int,
    val mes: Int,
    val kwhTotal: Double,
    val detalle: TarifaDetalle,
    var estado: EstadoBoleta = EstadoBoleta.EMITIDA
){

}
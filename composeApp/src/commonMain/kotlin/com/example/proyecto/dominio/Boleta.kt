package com.example.proyecto.dominio


/**
 * Boleta
 *
 * @property idCliente
 * @property anio
 * @property mes
 * @property kwhTotal
 * @property detalle
 * @property estado
 * @constructor Create empty Boleta
 */
class Boleta (
    val idCliente: String,
    val anio: Int,
    val mes: Int,
    val kwhTotal: Double,
    val detalle: TarifaDetalle,
    var estado: EstadoBoleta = EstadoBoleta.EMITIDA
)
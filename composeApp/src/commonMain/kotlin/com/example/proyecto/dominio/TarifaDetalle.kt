package com.example.proyecto.dominio

/**
 * Tarifa detalle
 *
 * @property kwh
 * @property subtotal
 * @property cargos
 * @property iva
 * @property total
 * @constructor Create empty Tarifa detalle
 */
data class TarifaDetalle (
    val kwh: Double,
    val subtotal: Double,
    val cargos: Double,
    val iva: Double,
    val total: Double
)
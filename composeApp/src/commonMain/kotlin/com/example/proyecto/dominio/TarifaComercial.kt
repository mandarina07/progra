package com.example.proyecto.dominio

/**
 * Tarifa comercial
 *
 * @constructor Create empty Tarifa comercial
 */
class TarifaComercial : Tarifa {

    override fun nombre(): String = "Tarifa Comercial"

    override fun calcular(kwh: Double): TarifaDetalle {
        val cargoFijo = 5000.0
        val costoKwh = 150.0
        val subtotal = kwh * costoKwh + cargoFijo
        val iva = subtotal * 0.19
        val total = subtotal + iva

        return TarifaDetalle(
            kwh = kwh,
            subtotal = subtotal,
            cargos = cargoFijo,
            iva = iva,
            total = total
        )
    }
}
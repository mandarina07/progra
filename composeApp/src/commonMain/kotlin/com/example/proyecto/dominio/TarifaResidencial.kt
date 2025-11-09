package com.example.proyecto.dominio

/**
 * Tarifa residencial
 *
 * @constructor Create empty Tarifa residencial
 */
class TarifaResidencial : Tarifa {

    override fun nombre(): String = "Tarifa Residencial"

    override fun calcular(kwh: Double): TarifaDetalle {
        val cargoFijo = 2500.0
        val costoKwh = 125.0
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

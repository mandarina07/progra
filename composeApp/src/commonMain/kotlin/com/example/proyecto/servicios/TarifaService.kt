package com.example.proyecto.servicios

import com.example.proyecto.dominio.Tarifa
import com.example.proyecto.dominio.TarifaComercial
import com.example.proyecto.dominio.TarifaDetalle
import com.example.proyecto.dominio.TarifaResidencial

/*
* Servicio que centraliza el uso de las tarifas
 * Aplica polimorfismo: TarifaResidencial y TarifaComercial implementan Tarifa
* */

class TarifaService {

    private val tarifaResidencial: Tarifa = TarifaResidencial()
    private val tarifaComercial: Tarifa = TarifaComercial()

    /**
     * Devuelve una tarifa según el tipo indicado.
     * tipoTarifa: "residencial" o "comercial"
     */
    fun obtenerTarifa(tipoTarifa: String): Tarifa =
        when (tipoTarifa.lowercase()) {
            "residencial" -> tarifaResidencial
            "comercial" -> tarifaComercial
            else -> tarifaResidencial   // por defecto
        }

    /**
     * Calcula el detalle de la boleta según el tipo de tarifa y los kWh.
     */
    fun calcularDetalle(tipoTarifa: String, kwh: Double): TarifaDetalle {
        val tarifa = obtenerTarifa(tipoTarifa)
        return tarifa.calcular(kwh)
    }
}
package com.example.proyecto.servicios

import com.example.proyecto.dominio.Tarifa
import com.example.proyecto.dominio.TarifaComercial
import com.example.proyecto.dominio.TarifaDetalle
import com.example.proyecto.dominio.TarifaResidencial

/**
 * Tarifa service
 *
 * @constructor Create empty Tarifa service
 */// Servicio que gestiona las tarifas y aplica polimorfismo entre residencial y comercial
class TarifaService {

    private val tarifaResidencial: Tarifa = TarifaResidencial()
    private val tarifaComercial: Tarifa = TarifaComercial()

    /**
     * Obtener tarifa
     *
     * @param tipoTarifa
     * @return
     */// Devuelve la tarifa según el tipo indicado
    fun obtenerTarifa(tipoTarifa: String): Tarifa =
        when (tipoTarifa.lowercase()) {
            "residencial" -> tarifaResidencial
            "comercial" -> tarifaComercial
            else -> tarifaResidencial // valor por defecto
        }

    /**
     * Calcular detalle
     *
     * @param tipoTarifa
     * @param kwh
     * @return
     */// Calcula el detalle de la boleta según la tarifa y el consumo
    fun calcularDetalle(tipoTarifa: String, kwh: Double): TarifaDetalle {
        val tarifa = obtenerTarifa(tipoTarifa)
        return tarifa.calcular(kwh)
    }
}

package com.example.proyecto.servicios

import com.example.proyecto.dominio.Boleta
import com.example.proyecto.dominio.EstadoBoleta
import com.example.proyecto.persistencia.BoletaRepositorio
import com.example.proyecto.persistencia.ClienteRepositorio
import com.example.proyecto.persistencia.LecturaRepositorio
import com.example.proyecto.persistencia.MedidorRepositorio

/**
 * Servicio de lógica de negocio para emitir boletas.
 * REVISAR
 */
/*
* Aquí asumimos que en tu clase Cliente existe una propiedad rut (
* que es lo típico y calza con el PDF).
*En Boleta, usamos idCliente = cliente.rut, como ya veníamos haciendo.
* */

class BoletaService(
    private val clienteRepositorio: ClienteRepositorio,
    private val medidorRepositorio: MedidorRepositorio,
    private val lecturaRepositorio: LecturaRepositorio,
    private val boletaRepositorio: BoletaRepositorio,
    private val tarifaService: TarifaService
) {

    /**
     * Calcula el total de kWh consumidos por TODOS los medidores
     * de un cliente en un mes/año determinado.
     */
    fun calcularKwhTotalCliente(rutCliente: String, anio: Int, mes: Int): Double {
        // Buscar todos los medidores asociados al cliente
        val medidores = medidorRepositorio.obtenerPorRut(rutCliente)

        // Para cada medidor, sumar el consumo del mes
        return medidores.sumOf { medidor ->
            val lecturas = lecturaRepositorio.listaPorMedidorMes(medidor.codigo, anio, mes)
            lecturas.sumOf { it.kwhLeidos }
        }
    }

    /**
     * Emite la boleta mensual para un cliente, considerando todos sus medidores.
     */
    fun emitirBoletaMensual(
        rutCliente: String,
        anio: Int,
        mes: Int,
        tipoTarifa: String
    ): Boleta {
        // Verificar que el cliente exista
        val cliente = clienteRepositorio.obtenerPorRut(rutCliente)
            ?: throw IllegalArgumentException("Cliente con RUT $rutCliente no existe")

        // Calcular el total de consumo de todos los medidores
        val kwhTotal = calcularKwhTotalCliente(rutCliente, anio, mes)

        // Calcular detalle según tarifa
        val detalle = tarifaService.calcularDetalle(tipoTarifa, kwhTotal)

        // Crear boleta
        val boleta = Boleta(
            idCliente = cliente.rut,
            anio = anio,
            mes = mes,
            kwhTotal = kwhTotal,
            detalle = detalle,
            estado = EstadoBoleta.EMITIDA
        )

        // Guardar en el repositorio
        boletaRepositorio.guardar(boleta)

        return boleta
    }

    /**
     * Lista todas las boletas de un cliente.
     */
    fun boletasDeCliente(rutCliente: String): List<Boleta> =
        boletaRepositorio.listaPorCliente(rutCliente)
}

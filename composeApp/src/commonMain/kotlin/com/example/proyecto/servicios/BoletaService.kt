package com.example.proyecto.servicios

import com.example.proyecto.dominio.Boleta
import com.example.proyecto.dominio.EstadoBoleta
import com.example.proyecto.persistencia.BoletaRepositorio
import com.example.proyecto.persistencia.ClienteRepositorio
import com.example.proyecto.persistencia.LecturaRepositorio
import com.example.proyecto.persistencia.MedidorRepositorio

/**
 * Boleta service
 *
 * @property clienteRepositorio
 * @property medidorRepositorio
 * @property lecturaRepositorio
 * @property boletaRepositorio
 * @property tarifaService
 * @constructor Create empty Boleta service
 */// Servicio encargado de generar y administrar las boletas
class BoletaService(
    private val clienteRepositorio: ClienteRepositorio,
    private val medidorRepositorio: MedidorRepositorio,
    private val lecturaRepositorio: LecturaRepositorio,
    private val boletaRepositorio: BoletaRepositorio,
    private val tarifaService: TarifaService
) {

    /**
     * Emitir boleta mensual
     *
     * @param rutCliente
     * @param anio
     * @param mes
     * @param tipoTarifa
     * @return
     */// Genera una boleta mensual para el cliente y la guarda en el repositorio
    fun emitirBoletaMensual(
        rutCliente: String,
        anio: Int,
        mes: Int,
        tipoTarifa: String
    ): Boleta {
        val cliente = clienteRepositorio.obtenerPorRut(rutCliente)
            ?: throw IllegalArgumentException("Cliente con RUT $rutCliente no existe")

        val kwhTotal = calcularKwhTotalCliente(rutCliente, anio, mes)
        val detalle = tarifaService.calcularDetalle(tipoTarifa, kwhTotal)

        val boleta = Boleta(
            idCliente = cliente.rut,
            anio = anio,
            mes = mes,
            kwhTotal = kwhTotal,
            detalle = detalle,
            estado = EstadoBoleta.EMITIDA
        )

        boletaRepositorio.guardar(boleta)
        return boleta
    }

    /**
     * Calcular kwh total cliente
     *
     * @param rutCliente
     * @param anio
     * @param mes
     * @return
     */// Calcula el consumo total (kWh) de todos los medidores del cliente en un mes especifico
    fun calcularKwhTotalCliente(
        rutCliente: String,
        anio: Int,
        mes: Int
    ): Double {
        val medidoresCliente = medidorRepositorio.obtenerPorRut(rutCliente)
        if (medidoresCliente.isEmpty()) return 0.0

        var total = 0.0
        medidoresCliente.forEach { medidor ->
            val lecturas = lecturaRepositorio.listaPorMedidorMes(
                idMedidor = medidor.codigo,
                anio = anio,
                mes = mes
            )
            lecturas.forEach { lectura ->
                total += lectura.kwhLeidos
            }
        }
        return total
    }

    /**
     * Boletas de cliente
     *
     * @param rutCliente
     * @return
     */// Devuelve todas las boletas asociadas a un cliente
    fun boletasDeCliente(rutCliente: String): List<Boleta> {
        return boletaRepositorio.listaPorCliente(rutCliente)
    }
}

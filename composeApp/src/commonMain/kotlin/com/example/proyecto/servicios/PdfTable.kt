package com.example.proyecto.servicios

import com.example.proyecto.dominio.Boleta
import com.example.proyecto.dominio.Cliente

/**
 * Pdf table
 *
 * @property headers
 * @property rows
 * @constructor Create empty Pdf table
 */// Estructura que representa una tabla para el PDF (encabezados y filas)
data class PdfTable(
    val headers: List<String>,
    val rows: List<List<String>>
)

/**
 * Build boletas pdf table
 *
 * @param boletas
 * @param clientes
 * @return
 */// Construye la tabla de boletas para generar el PDF
fun buildBoletasPdfTable(
    boletas: List<Boleta>,
    clientes: Map<String, Cliente>
): PdfTable {

    val headers = listOf(
        "RUT",
        "Nombre",
        "Mes",
        "Año",
        "kWh",
        "Subtotal",
        "Cargos",
        "IVA",
        "Total"
    )

    val rows = boletas.map { boleta ->
        val cliente = clientes[boleta.idCliente]
        listOf(
            cliente?.rut ?: boleta.idCliente,
            cliente?.nombre ?: "",
            boleta.mes.toString(),
            boleta.anio.toString(),
            boleta.kwhTotal.toString(),
            boleta.detalle.subtotal.toString(),
            boleta.detalle.cargos.toString(),
            boleta.detalle.iva.toString(),
            boleta.detalle.total.toString()
        )
    }

    return PdfTable(headers, rows)
}

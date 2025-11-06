package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Boleta

interface BoletaRepositorio {
    fun guardar(boleta: Boleta)
    fun obtener(rutCliente: String, anio: Int, mes: Int): Boleta?
    fun listaPorCliente(rutCliente: String): List<Boleta>
}
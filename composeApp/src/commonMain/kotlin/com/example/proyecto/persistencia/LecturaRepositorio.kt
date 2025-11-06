package com.example.proyecto.persistencia

import com.example.proyecto.dominio.LecturaConsumo

interface LecturaRepositorio {
    fun registrar(lectura: LecturaConsumo)
    fun listaPorMedidorMes(idMedidor: String, anio: Int, mes: Int): List<LecturaConsumo>
    fun ultimaLectura(idMedidor: String): LecturaConsumo?
}
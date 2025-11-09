package com.example.proyecto.dominio

/**
 * Lectura consumo
 *
 * @property idMedidor
 * @property anio
 * @property mes
 * @property kwhLeidos
 * @constructor Create empty Lectura consumo
 */
class LecturaConsumo (
    val idMedidor: String,
    val anio: Int,
    val mes: Int,
    val kwhLeidos: Double
)
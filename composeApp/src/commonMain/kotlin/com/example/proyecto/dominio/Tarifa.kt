package com.example.proyecto.dominio

/*
* Interface comun
* Aplica polimorfismo
* */

interface Tarifa {
    fun nombre(): String
    fun calcular(kwh: Double): TarifaDetalle

}
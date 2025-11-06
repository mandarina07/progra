package com.example.proyecto.dominio

/*
* CLASE BASE
* Usar String en lugar de Date para simplificar, ver luego
* */

open class EntidadBase (
    val id: String,
    val createdAt: String = "",
    val updatedAt: String = "",
){

}
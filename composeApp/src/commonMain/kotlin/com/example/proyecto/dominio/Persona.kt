package com.example.proyecto.dominio

/*
* Persona (cliente/operador)
* */

open class Persona(
    id: String,
    createdAt: String = "",
    updatedAt: String = "",
    val rut: String,
    var nombre: String,
    var email: String,
) : EntidadBase(id, createdAt, updatedAt) {

}
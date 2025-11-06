package com.example.proyecto.dominio

/*
* Operador del sistema
* */

class Operador(
    id: String,
    createdAt: String = "",
    updatedAt: String = "",
    rut: String,
    nombre: String,
    email: String,
    var perfil: String
) : Persona(id, createdAt, updatedAt, rut, nombre, email) {
}
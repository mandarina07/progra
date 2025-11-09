package com.example.proyecto.dominio

/**
 * Operador
 *
 * @property perfil
 * @constructor
 *
 * @param id
 * @param createdAt
 * @param updatedAt
 * @param rut
 * @param nombre
 * @param email
 */
class Operador(
    id: String,
    createdAt: String = "",
    updatedAt: String = "",
    rut: String,
    nombre: String,
    email: String,
    var perfil: String
) : Persona(id, createdAt, updatedAt, rut, nombre, email)
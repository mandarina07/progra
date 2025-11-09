package com.example.proyecto.dominio

/**
 * Persona
 *
 * @property rut
 * @property nombre
 * @property email
 * @constructor
 *
 * @param id
 * @param createdAt
 * @param updatedAt
 */
open class Persona(
    id: String,
    createdAt: String = "",
    updatedAt: String = "",
    val rut: String,
    var nombre: String,
    var email: String,
) : EntidadBase(id, createdAt, updatedAt)
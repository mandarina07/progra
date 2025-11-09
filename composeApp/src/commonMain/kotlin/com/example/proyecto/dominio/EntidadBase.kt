package com.example.proyecto.dominio

/*
* CLASE BASE
* Usar String en lugar de Date para simplificar
* */

/**
 * Entidad base
 *
 * @property id
 * @property createdAt
 * @property updatedAt
 * @constructor Create empty Entidad base
 */
open class EntidadBase (
    val id: String,
    val createdAt: String = "",
    val updatedAt: String = "",
)
package com.example.proyecto.dominio

/**
 * Cliente
 *
 * @property direccionFacturacion
 * @property estado
 * @constructor
 *
 * @param id
 * @param createdAt
 * @param updatedAt
 * @param rut
 * @param nombre
 * @param email
 */
class Cliente(
    id: String,
    createdAt: String = "",
    updatedAt: String = "",
    rut: String,
    nombre: String,
    email: String,
    var direccionFacturacion: String,
    var estado: EstadoCliente = EstadoCliente.ACTIVO

) : Persona(id, createdAt, updatedAt, rut, nombre, email)
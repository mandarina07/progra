package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Cliente

interface ClienteRepositorio {
    fun crear(cliente: Cliente)
    fun actualizar(cliente: Cliente)
    fun eliminar(rut: String): Boolean
    fun obtenerPorRut(rut: String): Cliente?
    fun listar(): List<Cliente>
}
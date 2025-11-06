package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Medidor

interface MedidorRepositorio {
    fun crear(medidor: Medidor)
    fun listar(): List<Medidor>
    fun obtenerPorRut(rutCliente: String): List<Medidor>
    fun eliminar(codigo: String): Boolean
}
package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Medidor

/**
 * Medidor repositorio
 *
 * @constructor Create empty Medidor repositorio
 */
interface MedidorRepositorio {
    /**
     * Crear
     *
     * @param medidor
     */
    fun crear(medidor: Medidor)

    /**
     * Listar
     *
     * @return
     */
    fun listar(): List<Medidor>

    /**
     * Obtener por rut
     *
     * @param rutCliente
     * @return
     */
    fun obtenerPorRut(rutCliente: String): List<Medidor>

    /**
     * Eliminar
     *
     * @param codigo
     * @return
     */
    fun eliminar(codigo: String): Boolean
}
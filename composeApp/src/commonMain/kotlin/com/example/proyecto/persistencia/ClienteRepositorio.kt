package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Cliente

/**
 * Cliente repositorio
 *
 * @constructor Create empty Cliente repositorio
 */
interface ClienteRepositorio {
    /**
     * Crear
     *
     * @param cliente
     */
    fun crear(cliente: Cliente)

    /**
     * Actualizar
     *
     * @param cliente
     */
    fun actualizar(cliente: Cliente)

    /**
     * Eliminar
     *
     * @param rut
     * @return
     */
    fun eliminar(rut: String): Boolean

    /**
     * Obtener por rut
     *
     * @param rut
     * @return
     */
    fun obtenerPorRut(rut: String): Cliente?

    /**
     * Listar
     *
     * @return
     */
    fun listar(): List<Cliente>
}
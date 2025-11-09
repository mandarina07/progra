package com.example.proyecto.persistencia

/**
 * Interfaz genérica de almacenamiento local.
 * Define las operaciones básicas de guardar, leer y eliminar datos.
 */
interface StorageDriver {

    fun put(key: String, data: ByteArray): Boolean

    fun get(key: String): ByteArray?

    fun keys(prefix: String): List<String>

    fun remove(key: String): Boolean
}

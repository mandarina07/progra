package com.example.proyecto.persistencia

/*
*  Abstraccion de un almacenamiento clave/valor
* USAMOS ESTO O NO? VER LUEGO
* */

interface StorageDriver {
    /**
     * Guarda datos binarios bajo una clave.
     */
    fun save(key: String, data: ByteArray): Boolean

    /**
     * Lee los datos asociados a una clave.
     */
    fun read(key: String): ByteArray?

    /**
     * Lista todas las claves que comienzan con el prefijo dado.
     */
    fun keys(prefix: String = ""): List<String>

    /**
     * Elimina la clave indicada.
     */
    fun delete(key: String): Boolean

}
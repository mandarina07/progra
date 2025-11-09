package com.example.proyecto.persistencia

import java.io.File

/**
 * File storage driver
 *
 * @constructor
 *
 * @param baseDirName
 */// Implementación local de StorageDriver que guarda datos como archivos .dat en Documentos/CGE
class FileStorageDriver(
    baseDirName: String = "Documentos/CGE"
) : StorageDriver {

    // Carpeta base donde se guardan los archivos
    private val baseDir: File

    init {
        val home = System.getProperty("user.home")
        baseDir = File(home, baseDirName)
        if (!baseDir.exists()) baseDir.mkdirs()
    }

    // Devuelve el archivo asociado a una clave
    private fun fileForKey(key: String): File =
        File(baseDir, "$key.dat")

    // Guarda los datos en un archivo con la clave dada
    override fun put(key: String, data: ByteArray): Boolean {
        return try {
            fileForKey(key).writeBytes(data)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Recupera los datos de un archivo segun su clave
    override fun get(key: String): ByteArray? {
        val file = fileForKey(key)
        return if (file.exists()) file.readBytes() else null
    }

    // Devuelve una lista de claves que comienzan con un prefijo dado
    override fun keys(prefix: String): List<String> {
        return baseDir.listFiles()
            ?.filter { it.name.startsWith(prefix) && it.name.endsWith(".dat") }
            ?.map { it.name.removeSuffix(".dat") }
            ?: emptyList()
    }

    // Elimina el archivo asociado a una clave
    override fun remove(key: String): Boolean {
        val file = fileForKey(key)
        return file.exists() && file.delete()
    }
}

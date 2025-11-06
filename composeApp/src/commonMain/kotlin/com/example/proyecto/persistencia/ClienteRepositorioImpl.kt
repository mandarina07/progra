package com.example.proyecto.persistencia

import com.example.proyecto.dominio.Cliente

class ClienteRepositorioImpl(
    private val persistenciaDatos: PersistenciaDatos
) : ClienteRepositorio {

    override fun crear(cliente: Cliente) {
        persistenciaDatos.agregarCliente(cliente)
    }

    override fun actualizar(cliente: Cliente) {
        persistenciaDatos.actualizarCliente(cliente)
    }

    override fun eliminar(rut: String): Boolean =
        persistenciaDatos.eliminarClientePorRut(rut)

    override fun obtenerPorRut(rut: String): Cliente? =
        persistenciaDatos.obtenerClientePorRut(rut)

    override fun listar(): List<Cliente> =
        persistenciaDatos.listarClientes()
}
package com.example.proyectointegrador.data.repository

import com.example.proyectointegrador.data.api.ApiClient
import com.example.proyectointegrador.data.api.ClienteApi
import com.example.proyectointegrador.data.dto.Cliente

interface ClienteRepository {
    suspend fun getClientes(): Result<List<Cliente>>
    suspend fun getClienteById(id: String): Result<Cliente>
    suspend fun agregarCliente(cliente: Cliente): Result<Cliente>
    suspend fun actualizarCliente(id: String, cliente: Cliente): Result<Cliente>
    suspend fun eliminarCliente(id: String): Result<Unit>
}

class ClienteRepositoryImpl(
    private val api: ClienteApi = ApiClient.clienteApi
) : ClienteRepository {

    override suspend fun getClientes(): Result<List<Cliente>> {
        return runCatching {
            api.getClientes()
        }
    }

    override suspend fun getClienteById(id: String): Result<Cliente> {
        return runCatching {
            api.getClienteById(id)
        }
    }

    override suspend fun agregarCliente(cliente: Cliente): Result<Cliente> {
        return runCatching {
            api.agregarCliente(cliente)
        }
    }

    override suspend fun actualizarCliente(id: String, cliente: Cliente): Result<Cliente> {
        return runCatching {
            api.actualizarCliente(id, cliente)
        }
    }

    override suspend fun eliminarCliente(id: String): Result<Unit> {
        return runCatching {
            api.eliminarCliente(id)
            Unit
        }
    }
}

package com.example.proyectointegrador.data.repository

import com.example.proyectointegrador.data.api.ApiClient
import com.example.proyectointegrador.data.api.VehiculoApi
import com.example.proyectointegrador.data.dto.Vehiculo

interface VehiculoRepository {
    suspend fun getVehiculos(): Result<List<Vehiculo>>
    suspend fun getVehiculoById(id: String): Result<Vehiculo>
    suspend fun agregarVehiculo(vehiculo: Vehiculo): Result<Vehiculo>
    suspend fun actualizarVehiculo(id: String, vehiculo: Vehiculo): Result<Vehiculo>
    suspend fun eliminarVehiculo(id: String): Result<Unit>
}

class VehiculoRepositoryImpl(
    private val api: VehiculoApi = ApiClient.vehiculoApi
) : VehiculoRepository {

    override suspend fun getVehiculos(): Result<List<Vehiculo>> {
        return runCatching {
            api.getVehiculos()
        }
    }

    override suspend fun getVehiculoById(id: String): Result<Vehiculo> {
        return runCatching {
            api.getVehiculoById(id)
        }
    }

    override suspend fun agregarVehiculo(vehiculo: Vehiculo): Result<Vehiculo> {
        return runCatching {
            api.agregarVehiculo(vehiculo)
        }
    }

    override suspend fun actualizarVehiculo(id: String, vehiculo: Vehiculo): Result<Vehiculo> {
        return runCatching {
            api.actualizarVehiculo(id, vehiculo)
        }
    }

    override suspend fun eliminarVehiculo(id: String): Result<Unit> {
        return runCatching {
            api.eliminarVehiculo(id)
            Unit
        }
    }
}

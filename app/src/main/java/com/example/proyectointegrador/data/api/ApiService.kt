package com.example.proyectointegrador.data.api

import com.example.proyectointegrador.data.dto.Cliente
import com.example.proyectointegrador.data.dto.Vehiculo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // --- Endpoints de Cliente ---

    @GET("cliente")
    suspend fun getClientes(): List<Cliente>

    @GET("cliente/{id}")
    suspend fun getClienteById(@Path("id") id: String): Cliente

    @POST("cliente")
    suspend fun agregarCliente(@Body cliente: Cliente): Cliente

    @PUT("cliente/{id}")
    suspend fun actualizarCliente(@Path("id") id: String, @Body cliente: Cliente): Cliente

    @DELETE("cliente/{id}")
    suspend fun eliminarCliente(@Path("id") id: String): Cliente

    // --- Endpoints de Vehiculo ---

    @GET("vehiculo")
    suspend fun getVehiculos(): List<Vehiculo>

    @GET("vehiculo/{id}")
    suspend fun getVehiculoById(@Path("id") id: String): Vehiculo

    @POST("vehiculo")
    suspend fun agregarVehiculo(@Body vehiculo: Vehiculo): Vehiculo

    @PUT("vehiculo/{id}")
    suspend fun actualizarVehiculo(@Path("id") id: String, @Body vehiculo: Vehiculo): Vehiculo

    @DELETE("vehiculo/{id}")
    suspend fun eliminarVehiculo(@Path("id") id: String): Vehiculo
}

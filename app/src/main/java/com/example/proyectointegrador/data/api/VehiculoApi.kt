package com.example.proyectointegrador.data.api

import com.example.proyectointegrador.data.dto.Vehiculo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VehiculoApi {

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

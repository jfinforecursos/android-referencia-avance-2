package com.example.proyectointegrador.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Vehiculo(
    val id: String = "",
    val marca: String,
    val modelo: String,
    val precioPorDia: Double,
    val disponible: Boolean = true
)

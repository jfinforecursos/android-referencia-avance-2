package com.example.proyectointegrador.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Cliente(
    val id: String = "",
    val nombre: String,
    val documento: String,
    val telefono: String
)

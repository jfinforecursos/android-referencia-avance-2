package com.example.proyectointegrador.data.network

import com.example.proyectointegrador.data.api.ApiClient
import com.example.proyectointegrador.data.api.ApiService

object RetrofitClient {
    val apiService: ApiService get() = ApiClient.apiService
}

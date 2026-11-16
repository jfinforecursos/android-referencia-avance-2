package com.example.proyectointegrador.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectointegrador.data.dto.Vehiculo
import com.example.proyectointegrador.data.repository.VehiculoRepository
import com.example.proyectointegrador.data.repository.VehiculoRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VehiculoViewModel(
    private val repository: VehiculoRepository = VehiculoRepositoryImpl()
) : ViewModel() {

    private val _vehiculos = MutableStateFlow<List<Vehiculo>>(emptyList())
    val vehiculos: StateFlow<List<Vehiculo>> = _vehiculos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        cargarVehiculos()
    }

    fun cargarVehiculos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.getVehiculos()
                .onSuccess { list ->
                    _vehiculos.value = list
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al cargar vehículos desde MockAPI"
                }
            _isLoading.value = false
        }
    }

    fun agregarVehiculo(marca: String, modelo: String, precioPorDia: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val nuevoVehiculo = Vehiculo(
                id = "",
                marca = marca,
                modelo = modelo,
                precioPorDia = precioPorDia
            )
            repository.agregarVehiculo(nuevoVehiculo)
                .onSuccess { vehiculoCreado ->
                    _vehiculos.value = _vehiculos.value + vehiculoCreado
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al guardar vehículo en MockAPI"
                }
            _isLoading.value = false
        }
    }

    fun actualizarVehiculo(id: String, marca: String, modelo: String, precioPorDia: Double, disponible: Boolean = true) {
        if (id.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val vehiculoActualizado = Vehiculo(
                id = id,
                marca = marca,
                modelo = modelo,
                precioPorDia = precioPorDia,
                disponible = disponible
            )
            repository.actualizarVehiculo(id, vehiculoActualizado)
                .onSuccess { vehiculoModificado ->
                    _vehiculos.value = _vehiculos.value.map {
                        if (it.id == id) vehiculoModificado else it
                    }
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al actualizar vehículo en MockAPI"
                }
            _isLoading.value = false
        }
    }

    fun eliminarVehiculo(id: String) {
        if (id.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.eliminarVehiculo(id)
                .onSuccess {
                    _vehiculos.value = _vehiculos.value.filter { it.id != id }
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al eliminar vehículo de MockAPI"
                }
            _isLoading.value = false
        }
    }
}

package com.example.proyectointegrador.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectointegrador.data.dto.Cliente
import com.example.proyectointegrador.data.repository.ClienteRepository
import com.example.proyectointegrador.data.repository.ClienteRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClienteViewModel(
    private val repository: ClienteRepository = ClienteRepositoryImpl()
) : ViewModel() {

    private val _clientes = MutableStateFlow<List<Cliente>>(emptyList())
    val clientes: StateFlow<List<Cliente>> = _clientes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        cargarClientes()
    }

    fun cargarClientes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.getClientes()
                .onSuccess { list ->
                    _clientes.value = list
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al cargar clientes desde MockAPI"
                }
            _isLoading.value = false
        }
    }

    fun agregarCliente(nombre: String, documento: String, telefono: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val nuevoCliente = Cliente(
                id = "",
                nombre = nombre,
                documento = documento,
                telefono = telefono
            )
            repository.agregarCliente(nuevoCliente)
                .onSuccess { clienteCreado ->
                    _clientes.value = _clientes.value + clienteCreado
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al guardar cliente en MockAPI"
                }
            _isLoading.value = false
        }
    }

    fun actualizarCliente(id: String, nombre: String, documento: String, telefono: String) {
        if (id.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val clienteActualizado = Cliente(
                id = id,
                nombre = nombre,
                documento = documento,
                telefono = telefono
            )
            repository.actualizarCliente(id, clienteActualizado)
                .onSuccess { clienteModificado ->
                    _clientes.value = _clientes.value.map {
                        if (it.id == id) clienteModificado else it
                    }
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al actualizar cliente en MockAPI"
                }
            _isLoading.value = false
        }
    }

    fun eliminarCliente(id: String) {
        if (id.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            repository.eliminarCliente(id)
                .onSuccess {
                    _clientes.value = _clientes.value.filter { it.id != id }
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al eliminar cliente de MockAPI"
                }
            _isLoading.value = false
        }
    }
}

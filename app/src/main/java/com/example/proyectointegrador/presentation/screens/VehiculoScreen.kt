package com.example.proyectointegrador.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectointegrador.data.dto.Vehiculo
import com.example.proyectointegrador.presentation.components.VehiculoCard
import com.example.proyectointegrador.presentation.viewmodel.VehiculoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculoScreen(
    viewModel: VehiculoViewModel,
    onNavigateBack: () -> Unit,
    onVerDetalle: (Vehiculo) -> Unit
) {
    val vehiculos by viewModel.vehiculos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var vehiculoAEditar by remember { mutableStateOf<Vehiculo?>(null) }
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    fun limpiarFormulario() {
        vehiculoAEditar = null
        marca = ""
        modelo = ""
        precio = ""
    }

    fun cargarVehiculoParaEdicion(vehiculo: Vehiculo) {
        vehiculoAEditar = vehiculo
        marca = vehiculo.marca
        modelo = vehiculo.modelo
        precio = vehiculo.precioPorDia.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Vehículos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }

            errorMessage?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (vehiculoAEditar != null) "Editar Vehículo" else "Registrar Nuevo Vehículo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (vehiculoAEditar != null) {
                    IconButton(onClick = { limpiarFormulario() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancelar edición")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca (ej. Toyota, Ford)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = modelo,
                onValueChange = { modelo = it },
                label = { Text("Modelo (ej. Corolla 2024)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio por Día ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val precioNum = precio.toDoubleOrNull()
                    if (marca.isNotBlank() && modelo.isNotBlank() && precioNum != null) {
                        val vehiculoActual = vehiculoAEditar
                        if (vehiculoActual != null) {
                            viewModel.actualizarVehiculo(
                                id = vehiculoActual.id,
                                marca = marca,
                                modelo = modelo,
                                precioPorDia = precioNum,
                                disponible = vehiculoActual.disponible
                            )
                        } else {
                            viewModel.agregarVehiculo(marca, modelo, precioNum)
                        }
                        limpiarFormulario()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (vehiculoAEditar != null) "Guardar Cambios" else "Agregar Vehículo")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Catálogo de Vehículos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (vehiculos.isEmpty()) {
                Text("No hay vehículos registrados.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                LazyColumn {
                    items(vehiculos) { vehiculo ->
                        VehiculoCard(
                            vehiculo = vehiculo,
                            onVerDetalle = { onVerDetalle(vehiculo) },
                            onEdit = { cargarVehiculoParaEdicion(vehiculo) },
                            onDelete = {
                                if (vehiculoAEditar?.id == vehiculo.id) {
                                    limpiarFormulario()
                                }
                                viewModel.eliminarVehiculo(vehiculo.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

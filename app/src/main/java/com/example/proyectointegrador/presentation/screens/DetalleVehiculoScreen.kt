package com.example.proyectointegrador.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleVehiculoScreen(
    id: String,
    marca: String,
    modelo: String,
    precioPorDia: Double,
    onNavigateBack: () -> Unit,
    onEditVehiculo: ((id: String, marca: String, modelo: String, precio: Double) -> Unit)? = null
) {
    var mostrarDialogoEditar by remember { mutableStateOf(false) }

    var marcaEdit by remember { mutableStateOf(marca) }
    var modeloEdit by remember { mutableStateOf(modelo) }
    var precioEdit by remember { mutableStateOf(precioPorDia.toString()) }

    var marcaState by remember { mutableStateOf(marca) }
    var modeloState by remember { mutableStateOf(modelo) }
    var precioState by remember { mutableStateOf(precioPorDia) }

    val alquiler3Dias = precioState * 3
    val alquiler7Dias = (precioState * 7) * 0.9

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Vehículo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        marcaEdit = marcaState
                        modeloEdit = modeloState
                        precioEdit = precioState.toString()
                        mostrarDialogoEditar = true
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Vehículo")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.tertiary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.DirectionsCar,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "$marcaState $modeloState",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    AssistChip(
                        onClick = { },
                        label = { Text("Disponible para Alquiler") }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    Text(
                        text = "ID Registro: $id",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Tarifa base diaria: $$precioState USD",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Estimaciones de Alquiler", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• 3 Días: $$alquiler3Dias USD")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• 7 Días (10% Desc.): $${"%.2f".format(alquiler7Dias)} USD")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = {
                    marcaEdit = marcaState
                    modeloEdit = modeloState
                    precioEdit = precioState.toString()
                    mostrarDialogoEditar = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Datos del Vehículo")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Listado")
            }
        }
    }

    if (mostrarDialogoEditar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEditar = false },
            title = { Text("Editar Vehículo") },
            text = {
                Column {
                    OutlinedTextField(
                        value = marcaEdit,
                        onValueChange = { marcaEdit = it },
                        label = { Text("Marca") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = modeloEdit,
                        onValueChange = { modeloEdit = it },
                        label = { Text("Modelo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = precioEdit,
                        onValueChange = { precioEdit = it },
                        label = { Text("Precio por Día ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nuevoPrecio = precioEdit.toDoubleOrNull()
                        if (marcaEdit.isNotBlank() && modeloEdit.isNotBlank() && nuevoPrecio != null) {
                            marcaState = marcaEdit
                            modeloState = modeloEdit
                            precioState = nuevoPrecio
                            onEditVehiculo?.invoke(id, marcaEdit, modeloEdit, nuevoPrecio)
                            mostrarDialogoEditar = false
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEditar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

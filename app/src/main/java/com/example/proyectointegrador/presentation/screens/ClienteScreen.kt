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
import com.example.proyectointegrador.data.dto.Cliente
import com.example.proyectointegrador.presentation.components.ClienteCard
import com.example.proyectointegrador.presentation.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(
    viewModel: ClienteViewModel,
    onNavigateBack: () -> Unit
) {
    val clientes by viewModel.clientes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var clienteAEditar by remember { mutableStateOf<Cliente?>(null) }
    var nombre by remember { mutableStateOf("") }
    var documento by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    fun limpiarFormulario() {
        clienteAEditar = null
        nombre = ""
        documento = ""
        telefono = ""
    }

    fun cargarClienteParaEdicion(cliente: Cliente) {
        clienteAEditar = cliente
        nombre = cliente.nombre
        documento = cliente.documento
        telefono = cliente.telefono
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Clientes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.secondary
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
                    text = if (clienteAEditar != null) "Editar Cliente" else "Registrar Nuevo Cliente",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (clienteAEditar != null) {
                    IconButton(onClick = { limpiarFormulario() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancelar edición")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = documento,
                onValueChange = { documento = it },
                label = { Text("Documento / Licencia") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono de Contacto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank() && documento.isNotBlank() && telefono.isNotBlank()) {
                        val clienteActual = clienteAEditar
                        if (clienteActual != null) {
                            viewModel.actualizarCliente(clienteActual.id, nombre, documento, telefono)
                        } else {
                            viewModel.agregarCliente(nombre, documento, telefono)
                        }
                        limpiarFormulario()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(if (clienteAEditar != null) "Guardar Cambios" else "Agregar Cliente")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Lista de Clientes", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (clientes.isEmpty()) {
                Text("No hay clientes registrados.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                LazyColumn {
                    items(clientes) { cliente ->
                        ClienteCard(
                            cliente = cliente,
                            onEdit = { cargarClienteParaEdicion(cliente) },
                            onDelete = {
                                if (clienteAEditar?.id == cliente.id) {
                                    limpiarFormulario()
                                }
                                viewModel.eliminarCliente(cliente.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

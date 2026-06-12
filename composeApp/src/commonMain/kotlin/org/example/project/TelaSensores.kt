package org.example.project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.project.viewmodel.SensorLeituraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaSensores(viewModel: SensorLeituraViewModel, onNavegarParaForm: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Central IoT 🌡️", fontWeight = FontWeight.Light, fontSize = 22.sp) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.limparCampos()
                    onNavegarParaForm()
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nova Leitura")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            if (viewModel.listaSensores.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
                        Text("Aguardando transmissões MQTT... 📡", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                items(viewModel.listaSensores) { leitura ->
                    val temperatura = leitura.temperatura.toString().toDoubleOrNull() ?: 25.0
                    val isCritico = temperatura < 24.0 || temperatura > 30.0
                    val isOffline = leitura.horaLeitura == "00:00"

                    val corDestaque = when {
                        isOffline -> Color.Gray
                        isCritico -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.secondary
                    }
                    val corFundoCard = if (isOffline) Color(0xFFF0F0F0) else MaterialTheme.colorScheme.surface

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = corFundoCard),
                        border = BorderStroke(1.dp, corDestaque.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                if (isOffline) {
                                    Surface(shape = RoundedCornerShape(8.dp), color = Color.LightGray) {
                                        Text("⚠️ Hardware Offline", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                    }
                                } else if (isCritico) {
                                    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                                            Icon(Icons.Default.Warning, contentDescription = "Alerta", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Ação Necessária", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                } else {
                                    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)) {
                                        Text("● Tempo Real", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                    }
                                }
                                Text("🕒 ${leitura.horaLeitura}", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "${leitura.temperatura}°C", fontSize = 46.sp, fontWeight = FontWeight.Light, color = corDestaque, modifier = Modifier.padding(end = 16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Ref: ${leitura.idAquario}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    IconButton(onClick = {
                                        viewModel.preencherFormulario(leitura)
                                        onNavegarParaForm()
                                    }, modifier = Modifier.size(36.dp)) {
                                        Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.outline)
                                    }
                                    IconButton(onClick = {
                                        viewModel.apagar(leitura.id)
                                        coroutineScope.launch { snackbarHostState.showSnackbar("🗑️ Leitura apagada.") }
                                    }, modifier = Modifier.size(36.dp)) {
                                        Icon(Icons.Default.Delete, contentDescription = "Apagar", tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f))
                                    }
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}
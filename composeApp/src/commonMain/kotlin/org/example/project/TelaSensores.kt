package org.example.project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.project.viewmodel.SensorLeituraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaSensores(viewModel: SensorLeituraViewModel) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var mostrarGaveta by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    // Substituímos o coqueiro/bambu pelo termómetro aqui!
                    title = { Text("Central IoT 🌡️", fontWeight = FontWeight.Light, fontSize = 22.sp) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )

            },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarGaveta = true },
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
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${leitura.temperatura}°",
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Ref: ${leitura.idAquario}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("🕒 ${leitura.horaLeitura}", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                IconButton(onClick = {
                                    viewModel.preencherFormulario(leitura)
                                    mostrarGaveta = true
                                }, modifier = Modifier.size(36.dp)) {
                                    Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.secondary)
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
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        if (mostrarGaveta) {
            ModalBottomSheet(
                onDismissRequest = {
                    mostrarGaveta = false
                    viewModel.limparCampos()
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(24.dp).padding(bottom = 32.dp)) {
                    Text("Registo Manual de Sensor", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 16.dp))

                    OutlinedTextField(
                        value = viewModel.formIdAquario,
                        onValueChange = { viewModel.formIdAquario = it },
                        label = { Text("ID do Aquário") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = viewModel.formTemperatura,
                            onValueChange = { viewModel.formTemperatura = it },
                            label = { Text("Temp (°C)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = viewModel.formHoraLeitura,
                            onValueChange = { viewModel.formHoraLeitura = it },
                            label = { Text("Hora (HH:mm)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.gravar()
                            coroutineScope.launch {
                                sheetState.hide()
                                mostrarGaveta = false
                                viewModel.limparCampos()
                                snackbarHostState.showSnackbar("📡 Leitura gravada com sucesso!")
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Submeter Dados", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
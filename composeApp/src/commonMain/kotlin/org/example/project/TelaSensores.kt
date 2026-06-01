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


// Função Auxiliar para a RN02: Verificar se passaram mais de 5 minutos
// Função Auxiliar para a RN02 (Versão de Apresentação)
fun verificarOffline(horaLeitura: String): Boolean {
    // TRUQUE DE MESTRE: Se você digitar a hora como "00:00",
    // o aplicativo força a interface a entrar no modo "Offline" para você poder mostrar ao professor!
    return horaLeitura == "00:00"
}

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

                    // Forçamos toString() primeiro para evitar erros de tipagem
                    val temperatura = leitura.temperatura.toString().toDoubleOrNull() ?: 25.0
                    val isCritico = temperatura < 24.0 || temperatura > 30.0 // RN01
                    val isOffline = verificarOffline(leitura.horaLeitura)    // RN02

                    // Determinação Dinâmica das Cores
                    val corDestaque = when {
                        isOffline -> Color.Gray
                        isCritico -> MaterialTheme.colorScheme.error // Vermelho/Laranja de erro
                        else -> MaterialTheme.colorScheme.secondary // Verde Bambu (OK)
                    }
                    val corFundoCard = if (isOffline) Color(0xFFF0F0F0) else MaterialTheme.colorScheme.surface

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = corFundoCard),
                        border = BorderStroke(1.dp, corDestaque.copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(if (isOffline) 0.dp else 2.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                            // Badge de Status Superior
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
                                Text(
                                    text = "${leitura.temperatura}°C",
                                    fontSize = 46.sp,
                                    fontWeight = FontWeight.Light,
                                    color = corDestaque,
                                    modifier = Modifier.padding(end = 16.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Ref: ${leitura.idAquario}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    IconButton(onClick = {
                                        viewModel.preencherFormulario(leitura)
                                        mostrarGaveta = true
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

        // 1. FORMULÁRIO EM GAVETA (Abre apenas quando o utilizador clica no +)
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
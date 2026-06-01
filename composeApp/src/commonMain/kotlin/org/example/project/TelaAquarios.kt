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
import org.example.project.viewmodel.AquarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAquarios(viewModel: AquarioViewModel) {
    // Controlos da Gaveta (Bottom Sheet) e Snackbar (Avisos)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var mostrarGaveta by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, // 3. Feedback Visual
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Os Meus Aquários", fontWeight = FontWeight.Light, fontSize = 22.sp) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarGaveta = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(Icons.Default.Add, contentDescription = "Novo Aquário")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        // A lista agora ocupa o ecrã inteiro e é muito mais limpa!
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            if (viewModel.listaAquarios.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
                        Text("A água está calma. Adicione a sua primeira carpa. 🎏", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                items(viewModel.listaAquarios) { aquario ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(aquario.nome, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
                                Row {
                                    IconButton(onClick = {
                                        viewModel.preencherFormulario(aquario)
                                        mostrarGaveta = true
                                    }, modifier = Modifier.size(24.dp)) {
                                        Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.secondary)
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    IconButton(onClick = {
                                        viewModel.apagar(aquario.id)
                                        coroutineScope.launch { snackbarHostState.showSnackbar("🗑️ Aquário apagado.") }
                                    }, modifier = Modifier.size(24.dp)) {
                                        Icon(Icons.Default.Delete, contentDescription = "Apagar", tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${aquario.capacidadeLitros} Litros • Desde ${aquario.dataInstalacao}", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)) {
                                    Text("💧 Água Cristalina", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) } // Espaço para o FAB não cobrir os cards
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
                    Text("Configurar Aquário", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 16.dp))

                    OutlinedTextField(
                        value = viewModel.formNome,
                        onValueChange = { viewModel.formNome = it },
                        label = { Text("Identificação") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = viewModel.formCapacidade,
                            onValueChange = { viewModel.formCapacidade = it },
                            label = { Text("Volume (L)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = viewModel.formInstalacao,
                            onValueChange = { viewModel.formInstalacao = it },
                            label = { Text("Data") },
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
                                snackbarHostState.showSnackbar("✔️ Aquário registado com sucesso!")
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Salvar Registo", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
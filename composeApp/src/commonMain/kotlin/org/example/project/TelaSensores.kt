package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.viewmodel.SensorLeituraViewModel

@Composable
fun TelaSensores(viewModel: SensorLeituraViewModel) {

    var abaSelecionada by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        TabRow(selectedTabIndex = abaSelecionada) {
            Tab(
                selected = abaSelecionada == 0,
                onClick = { abaSelecionada = 0 },
                text = { Text("Formulário") }
            )
            Tab(
                selected = abaSelecionada == 1,
                onClick = { abaSelecionada = 1 },
                text = { Text("Lista de Sensores") }
            )
        }

        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            if (abaSelecionada == 0) {
                Column {
                    Text("Cadastrar Leitura", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = viewModel.formIdAquario,
                        onValueChange = { viewModel.formIdAquario = it },
                        label = { Text("ID do Aquário") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = viewModel.formTemperatura,
                        onValueChange = { viewModel.formTemperatura = it },
                        label = { Text("Temperatura (ºC)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = viewModel.formHoraLeitura,
                        onValueChange = { viewModel.formHoraLeitura = it },
                        label = { Text("Hora da Leitura (Ex: 14:30)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(onClick = {
                            viewModel.gravar()
                            abaSelecionada = 1
                        }) {
                            Text("Gravar")
                        }
                        OutlinedButton(onClick = { viewModel.limparCampos() }) {
                            Text("Limpar")
                        }
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewModel.listaSensores) { sensor ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("ID Aquário: ${sensor.idAquario}", style = MaterialTheme.typography.titleMedium)
                                    Text("Temperatura: ${sensor.temperatura} ºC")
                                    Text("Hora: ${sensor.horaLeitura}")
                                }
                                Row {
                                    IconButton(onClick = {
                                        viewModel.preencherFormulario(sensor)
                                        abaSelecionada = 0
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                                    }
                                    IconButton(onClick = { viewModel.apagar(sensor.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Apagar", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
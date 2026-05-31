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
import org.example.project.viewmodel.AquarioViewModel

@Composable
fun TelaAquarios(viewModel: AquarioViewModel) {

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gerenciar Aquários", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // ==========================================
        // 1. FORMULÁRIO (Requisito H)
        // ==========================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = viewModel.formNome,
                    onValueChange = { viewModel.formNome = it },
                    label = { Text("Nome do Aquário") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = viewModel.formCapacidade,
                    onValueChange = { viewModel.formCapacidade = it },
                    label = { Text("Capacidade (Litros)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = viewModel.formInstalacao,
                    onValueChange = { viewModel.formInstalacao = it },
                    label = { Text("Data de Instalação") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        viewModel.gravar()
                    }) {
                        Text("Gravar")
                    }
                    OutlinedButton(onClick = { viewModel.limparCampos() }) {
                        Text("Limpar")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Meus Aquários", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // ==========================================
        // 2. LISTAGEM COM CARDS E ÍCONES (Requisito I, J e K)
        // ==========================================
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.listaAquarios) { aquario ->
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
                            // Aqui garantimos os 3 campos exigidos pelo PDF!
                            Text("Nome: ${aquario.nome}", style = MaterialTheme.typography.titleMedium)
                            Text("Capacidade: ${aquario.capacidadeLitros} L")
                            Text("Instalação: ${aquario.dataInstalacao}")
                        }

                        Row {
                            IconButton(onClick = {
                                viewModel.preencherFormulario(aquario)
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = {
                                viewModel.apagar(aquario.id)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Apagar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
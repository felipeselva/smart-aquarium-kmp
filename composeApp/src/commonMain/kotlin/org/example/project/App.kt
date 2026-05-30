package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.viewmodel.AquarioViewModel

@Composable
fun App() {
    MaterialTheme {
        // Instancia o nosso ViewModel (que vai disparar a busca na API)
        val viewModel = remember { AquarioViewModel() }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Text(
                text = "Meus Aquários Inteligentes",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Pega a lista que o ViewModel buscou na internet
            val aquarios = viewModel.listaAquarios

            if (aquarios.isEmpty()) {
                Text("Buscando dados ou nenhum aquário encontrado...\n(A sua API do Spring Boot está ligada?)")
            } else {
                // Desenha uma lista rolável na tela
                LazyColumn {
                    items(aquarios) { aquario ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Nome: ${aquario.nome}", style = MaterialTheme.typography.titleLarge)
                                Text(text = "ID: ${aquario.id}")
                                Text(text = "Capacidade: ${aquario.capacidadeLitros}L")
                                Text(text = "Água Salgada: ${if (aquario.aguaSalgada) "Sim" else "Não"}")
                                Text(text = "Instalação: ${aquario.dataInstalacao}")
                            }
                        }
                    }
                }
            }
        }
    }
}
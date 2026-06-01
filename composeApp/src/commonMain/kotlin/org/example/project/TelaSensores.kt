package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.viewmodel.SensorLeituraViewModel

@Composable
fun TelaSensores(viewModel: SensorLeituraViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Leituras dos Sensores", style = MaterialTheme.typography.headlineMedium)

        // Formulário (Movid de fora de blocos lógicos para evitar erro @Composable)
        OutlinedTextField(
            value = viewModel.formIdAquario,
            onValueChange = { viewModel.formIdAquario = it },
            label = { Text("ID do Aquário") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.formTemperatura,
            onValueChange = { viewModel.formTemperatura = it },
            label = { Text("Temperatura") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.formHoraLeitura,
            onValueChange = { viewModel.formHoraLeitura = it },
            label = { Text("Hora da Leitura") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { viewModel.gravar() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Gravar Leitura")
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        LazyColumn {
            items(viewModel.listaSensores) { leitura ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Aquário: ${leitura.idAquario}")
                        Text("Temp: ${leitura.temperatura}°C")
                        Text("Hora: ${leitura.horaLeitura}")
                        Row {
                            Button(onClick = { viewModel.preencherFormulario(leitura) }) { Text("Editar") }
                            Button(onClick = { viewModel.apagar(leitura.id) }) { Text("Apagar") }
                        }
                    }
                }
            }
        }
    }
}
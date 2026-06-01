package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.example.project.repository.RepositorioRemoto
import org.example.project.viewmodel.*

@Composable
fun App() {
    MaterialTheme {
        val repo = remember { RepositorioRemoto() }
        var telaAtual by remember { mutableStateOf("login") }

        val loginVM = remember { LoginViewModel(repo) }
        val aquarioVM = remember { AquarioViewModel(repo) }
        val sensorVM = remember { SensorLeituraViewModel(repo) }

        when (telaAtual) {
            "login" -> TelaLogin(
                viewModel = loginVM,
                onNavegarParaAquarios = { telaAtual = "aquarios" }
            )
            "aquarios" -> {
                LaunchedEffect(Unit) { aquarioVM.carregarAquarios() }
                TelaAquarios(
                    viewModel = aquarioVM,
                    onNavegarParaSensores = { telaAtual = "sensores" } // O "fio" para ir para os sensores
                )
            }
            "sensores" -> {
                LaunchedEffect(Unit) { sensorVM.carregarLeituras() }
                TelaSensores(
                    viewModel = sensorVM,
                    onVoltar = { telaAtual = "aquarios" } // O "fio" para voltar para os aquários
                )
            }
        }
    }
}
package org.example.project

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import org.example.project.repository.RepositorioRemoto
import org.example.project.viewmodel.*

// CORES DO TEMA ASIÁTICO/ZEN
val AzulProfundo = Color(0xFF00416A)
val LaranjaKoi = Color(0xFFE95C4B)
val FundoAreia = Color(0xFFF9F6F0)
val VerdeBambu = Color(0xFF2E8B57)

private val TemaZenAquario = lightColorScheme(
    primary = AzulProfundo,
    secondary = VerdeBambu,
    tertiary = LaranjaKoi,
    background = FundoAreia,
    surface = Color.White,
    error = LaranjaKoi
)

@Composable
fun App() {
    MaterialTheme(colorScheme = TemaZenAquario) {
        val repo = remember { RepositorioRemoto() }
        var telaAtual by remember { mutableStateOf("login") }

        val loginVM = remember { LoginViewModel(repo) }
        val aquarioVM = remember { AquarioViewModel(repo) }
        val sensorVM = remember { SensorLeituraViewModel(repo) }

        val fazerLogout = {
            repo.tokenJwt = null
            telaAtual = "login"
        }

        Surface(color = MaterialTheme.colorScheme.background) {
            if (telaAtual == "login") {
                TelaLogin(
                    viewModel = loginVM,
                    onNavegarParaAquarios = { telaAtual = "home" }
                )
            } else {
                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ) {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
                                label = { Text("Início") },
                                selected = telaAtual == "home",
                                onClick = { telaAtual = "home" },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = AzulProfundo, indicatorColor = AzulProfundo.copy(alpha = 0.1f))
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.List, contentDescription = "Aquários") },
                                label = { Text("Aquários") },
                                selected = telaAtual == "aquarios",
                                onClick = { telaAtual = "aquarios" },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = AzulProfundo, indicatorColor = AzulProfundo.copy(alpha = 0.1f))
                            )
                            // 🔥 Ícone de Alerta (Warning/Triângulo) no menu inferior
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Warning, contentDescription = "IoT") },
                                label = { Text("IoT") },
                                selected = telaAtual == "sensores",
                                onClick = { telaAtual = "sensores" },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = AzulProfundo, indicatorColor = AzulProfundo.copy(alpha = 0.1f))
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                                label = { Text("Perfil") },
                                selected = telaAtual == "perfil",
                                onClick = { telaAtual = "perfil" },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = AzulProfundo, indicatorColor = AzulProfundo.copy(alpha = 0.1f))
                            )
                        }
                    }
                ) { paddingValues ->
                    Crossfade(
                        targetState = telaAtual,
                        modifier = Modifier.padding(paddingValues),
                        label = "AnimacaoAbas"
                    ) { tela ->
                        when (tela) {
                            "home" -> TelaHome(
                                onNavegarParaAquarios = { telaAtual = "aquarios" },
                                onNavegarParaSensores = { telaAtual = "sensores" },
                                onSair = fazerLogout
                            )
                            "aquarios" -> {
                                LaunchedEffect(Unit) { aquarioVM.carregarAquarios() }
                                TelaAquarios(viewModel = aquarioVM)
                            }
                            "sensores" -> {
                                LaunchedEffect(Unit) { sensorVM.carregarLeituras() }
                                TelaSensores(viewModel = sensorVM)
                            }
                            "perfil" -> TelaPerfil(onSair = fazerLogout)
                        }
                    }
                }
            }
        }
    }
}
package org.example.project

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.example.project.model.SessaoUsuario
import org.example.project.repository.*
import org.example.project.viewmodel.*

// Suas cores originais
val AzulProfundo = Color(0xFF00416A)
val LaranjaKoi = Color(0xFFE95C4B)
val FundoAreia = Color(0xFFF9F6F0)
val VerdeBambu = Color(0xFF2E8B57)

private val TemaZenAquario = lightColorScheme(
    primary = AzulProfundo, secondary = VerdeBambu, tertiary = LaranjaKoi,
    background = FundoAreia, surface = Color.White, error = LaranjaKoi
)

@Composable
fun App() {
    MaterialTheme(colorScheme = TemaZenAquario) {
        val navController = rememberNavController()

        // 1. Injeção de Dependências Manual Limpa
        val sessao = remember { SessaoUsuario() }
        val authRepo = remember { AuthRepositorioRemoto(sessao) }
        val aquarioRepo = remember { AquarioRepositorioRemoto(sessao) }
        val sensorRepo = remember { SensorRepositorioRemoto(sessao) }

        val loginVM = remember { LoginViewModel(authRepo) }
        val cadastroVM = remember { CadastroViewModel(authRepo) }
        val aquarioVM = remember { AquarioViewModel(aquarioRepo) }
        val sensorVM = remember { SensorLeituraViewModel(sensorRepo) }

        val rotasComBarra = listOf("home", "aquarios_lista", "sensores_lista", "perfil")
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val rotaAtual = navBackStackEntry?.destination?.route

        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                bottomBar = {
                    if (rotasComBarra.contains(rotaAtual)) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ) {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Home, "Início") }, label = { Text("Início") },
                                selected = rotaAtual == "home", onClick = { navController.navigate("home") }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.List, "Aquários") }, label = { Text("Aquários") },
                                selected = rotaAtual == "aquarios_lista", onClick = { navController.navigate("aquarios_lista") }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Warning, "IoT") }, label = { Text("IoT") },
                                selected = rotaAtual == "sensores_lista", onClick = { navController.navigate("sensores_lista") }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Person, "Perfil") }, label = { Text("Perfil") },
                                selected = rotaAtual == "perfil", onClick = { navController.navigate("perfil") }
                            )
                        }
                    }
                }
            ) { paddingValues ->
                // NAVEGAÇÃO NÍVEL 1 e NÍVEL 2 (Regra 1.d explícita do PDF)
                NavHost(
                    navController = navController,
                    startDestination = "login",
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable("login") {
                        TelaLogin(viewModel = loginVM,
                            onNavegarParaAquarios = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                            onNavegarParaCadastro = { navController.navigate("cadastro") }
                        )
                    }
                    composable("cadastro") {
                        TelaCadastro(viewModel = cadastroVM,
                            onVoltar = { navController.popBackStack() },
                            onCadastrarSucesso = { navController.popBackStack() }
                        )
                    }
                    composable("home") {
                        TelaHome(
                            onNavegarParaAquarios = { navController.navigate("aquarios_lista") },
                            onNavegarParaSensores = { navController.navigate("sensores_lista") },
                            onSair = { sessao.tokenJwt = null; navController.navigate("login") { popUpTo(0) } }
                        )
                    }
                    // NÍVEL 2: Listagem vs Formulário
                    composable("aquarios_lista") {
                        LaunchedEffect(Unit) { aquarioVM.carregarAquarios() }
                        TelaAquarios(viewModel = aquarioVM, onNavegarParaForm = { navController.navigate("aquarios_form") })
                    }
                    composable("aquarios_form") {
                        TelaAquariosForm(viewModel = aquarioVM, onVoltar = { navController.popBackStack() })
                    }
                    composable("sensores_lista") {
                        LaunchedEffect(Unit) { sensorVM.carregarLeituras() }
                        TelaSensores(viewModel = sensorVM, onNavegarParaForm = { navController.navigate("sensores_form") })
                    }
                    composable("sensores_form") {
                        TelaSensoresForm(viewModel = sensorVM, onVoltar = { navController.popBackStack() })
                    }
                    composable("perfil") {
                        TelaPerfil(onSair = { sessao.tokenJwt = null; navController.navigate("login") { popUpTo(0) } })
                    }
                }
            }
        }
    }
}
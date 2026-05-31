package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.viewmodel.AquarioViewModel
import org.example.project.viewmodel.SensorLeituraViewModel

@Composable
fun App() {
    MaterialTheme {
        // Esse é o "Navigation3" exigido pelo professor para controlar as telas
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "login") {

            // TELA DE LOGIN
            composable("login") {
                TelaLogin(
                    onIrParaRegistro = { navController.navigate("registro") },
                    onLoginSucesso = { navController.navigate("menu_cruds") }
                )
            }

            // TELA DE REGISTRO
            composable("registro") {
                TelaRegistro(
                    onIrParaLogin = { navController.navigate("login") },
                    onRegistroSucesso = { navController.navigate("menu_cruds") }
                )
            }

            // MENU PRINCIPAL DOS CRUDS (Requisito D - Navegação Nível 1)
            composable("menu_cruds") {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Painel de Controle", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { navController.navigate("crud_aquarios") },
                        modifier = Modifier.fillMaxWidth().height(60.dp)
                    ) {
                        Text("Gerenciar Aquários")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigate("crud_sensores") },
                        modifier = Modifier.fillMaxWidth().height(60.dp)
                    ) {
                        Text("Gerenciar Sensores")
                    }
                }
            }

            // ROTA DO CRUD DE AQUÁRIOS
            composable("crud_aquarios") {
                val aquarioViewModel = remember { AquarioViewModel() }
                TelaAquarios(viewModel = aquarioViewModel)
            }

            // ROTA DO CRUD DE SENSORES
            composable("crud_sensores") {
                val sensorViewModel = remember { SensorLeituraViewModel() }
                TelaSensores(viewModel = sensorViewModel)
            }
        }
    }
}

// ==========================================
// COMPONENTE: TELA DE LOGIN (Requisito A)
// ==========================================
@Composable
fun TelaLogin(onIrParaRegistro: () -> Unit, onLoginSucesso: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo ao Aquário!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(), // Oculta a senha
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Botão de Logar
        Button(onClick = onLoginSucesso, modifier = Modifier.fillMaxWidth()) {
            Text("Entrar")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Botão para ir ao Registro
        TextButton(onClick = onIrParaRegistro) {
            Text("Ainda não tem conta? Registre-se")
        }
    }
}

// ==========================================
// COMPONENTE: TELA DE REGISTRO (Requisito B)
// ==========================================
@Composable
fun TelaRegistro(onIrParaLogin: () -> Unit, onRegistroSucesso: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmaSenha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Criar Nova Conta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nome, onValueChange = { nome = it },
            label = { Text("Nome") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = senha, onValueChange = { senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmaSenha, onValueChange = { confirmaSenha = it },
            label = { Text("Confirmar Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onRegistroSucesso, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar")
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onIrParaLogin) {
            Text("Já possui conta? Faça o Login")
        }
    }
}
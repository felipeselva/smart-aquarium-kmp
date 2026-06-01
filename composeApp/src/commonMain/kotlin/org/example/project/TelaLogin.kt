package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.example.project.viewmodel.LoginViewModel

@Composable
fun TelaLogin(
    viewModel: LoginViewModel,
    onNavegarParaAquarios: () -> Unit // Função de GPS para trocar de tela
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Smart Aquarium",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de E-mail
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Senha
        OutlinedTextField(
            value = viewModel.senha,
            onValueChange = { viewModel.senha = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(), // Esconde a senha com bolinhas
            modifier = Modifier.fillMaxWidth()
        )

        // Exibe o erro profissionalmente caso o usuário esqueça de preencher
        if (viewModel.mensagemErro != null) {
            Text(
                text = viewModel.mensagemErro!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botão de Entrar
        Button(
            onClick = {
                viewModel.fazerLogin(onLoginSucesso = onNavegarParaAquarios)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Entrar")
        }
    }
}
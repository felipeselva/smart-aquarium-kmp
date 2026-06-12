package org.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.repository.AuthRepositorioRemoto

class CadastroViewModel(private val repository: AuthRepositorioRemoto) {
    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmacaoSenha by mutableStateOf("")
    var mensagemErro by mutableStateOf<String?>(null)

    fun cadastrar(onSucesso: () -> Unit) {
        if (nome.isBlank() || email.isBlank() || senha.isBlank() || confirmacaoSenha.isBlank()) {
            mensagemErro = "Por favor, preencha todos os campos. 🎋"
            return
        }

        if (senha != confirmacaoSenha) {
            mensagemErro = "As senhas não coincidem!"
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val sucesso = repository.cadastrarUsuario(nome, email, senha)
                if (sucesso) {
                    mensagemErro = null
                    onSucesso()
                } else {
                    mensagemErro = "Erro ao criar conta. Tente outro e-mail."
                }
            } catch (e: Exception) {
                mensagemErro = "Erro de conexão com o servidor."
            }
        }
    }

    fun limparCampos() {
        nome = ""; email = ""; senha = ""; confirmacaoSenha = ""; mensagemErro = null
    }
}
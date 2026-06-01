package org.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.repository.RepositorioRemoto

class CadastroViewModel(private val repository: RepositorioRemoto) {
    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var mensagemErro by mutableStateOf<String?>(null)

    fun cadastrar(onSucesso: () -> Unit) {
        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            mensagemErro = "Por favor, preencha todos os campos. 🎋"
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                // 🔥 AQUI ESTÁ A MÁGICA: Chamando a função real do Repositório!
                val sucesso = repository.cadastrarUsuario(nome, email, senha)

                if (sucesso) {
                    mensagemErro = null
                    onSucesso() // Sucesso! O App.kt vai redirecionar o utilizador para o Login
                } else {
                    mensagemErro = "Erro ao criar conta. O e-mail pode já estar em uso."
                }
            } catch (e: Exception) {
                mensagemErro = "Erro de conexão com o servidor."
            }
        }
    }

    fun limparCampos() {
        nome = ""
        email = ""
        senha = ""
        mensagemErro = null
    }
}
package org.example.project.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.repository.RepositorioRemoto

class LoginViewModel(private val repositorio: RepositorioRemoto) : ViewModel() {

    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var mensagemErro by mutableStateOf<String?>(null)

    fun fazerLogin(onLoginSucesso: () -> Unit) {
        viewModelScope.launch {
            val sucesso = repositorio.fazerLogin(email, senha)
            if (sucesso) {
                mensagemErro = null
                onLoginSucesso()
            } else {
                mensagemErro = "E-mail ou senha incorretos!"
            }
        }
    }
}
package org.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.model.Aquario
import org.example.project.repository.RepositorioRemoto

class AquarioViewModel : ViewModel() {

    // Instancia o nosso motoqueiro (Ktor) para fazer as requisições
    private val repositorio = RepositorioRemoto()

    // 1. Variável de estado para guardar a lista de aquários que vem da API
    var listaAquarios by mutableStateOf(listOf<Aquario>())
        private set

    // 2. Variáveis de estado para o formulário da tela
    var formId by mutableStateOf("")
    var formNome by mutableStateOf("")
    var formCapacidade by mutableStateOf("")
    var formAguaSalgada by mutableStateOf(false)
    var formInstalacao by mutableStateOf("")

    init {
        // Assim que a tela abrir, ele já vai na API buscar os dados
        carregarAquarios()
    }

    // ==========================================
    // FUNÇÃO QUE VAI NA INTERNET (GET)
    // ==========================================
    fun carregarAquarios() {
        viewModelScope.launch {
            try {
                // Vai na internet buscar a lista JSON
                val listaDaInternet = repositorio.buscarAquarios()

                // Pega a lista que veio do Spring Boot e joga na tela!
                listaAquarios = listaDaInternet
            } catch (e: Exception) {
                println("Erro ao buscar dados da API: ${e.message}")
            }
        }
    }

    // ==========================================
    // FUNÇÕES DA TELA (Formulário)
    // ==========================================
    fun preencherFormulario(aquario: Aquario) {
        formId = aquario.id
        formNome = aquario.nome
        formCapacidade = aquario.capacidadeLitros.toString()
        formAguaSalgada = aquario.aguaSalgada
        formInstalacao = aquario.dataInstalacao
    }

    fun limparCampos() {
        formId = ""
        formNome = ""
        formCapacidade = ""
        formAguaSalgada = false
        formInstalacao = ""
    }

    // Obs: As funções gravar() e apagar() precisarão ser adaptadas
    // depois para usar o Ktor (POST e DELETE). O foco agora é listar (GET)!
}
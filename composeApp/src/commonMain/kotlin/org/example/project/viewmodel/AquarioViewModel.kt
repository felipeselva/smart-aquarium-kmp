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

    private val repositorio = RepositorioRemoto()

    var listaAquarios by mutableStateOf(listOf<Aquario>())
        private set

    // Tratamos o formId corretamente como String opcional
    var formId by mutableStateOf<String?>(null)
    var formNome by mutableStateOf("")
    var formCapacidade by mutableStateOf("")
    var formAguaSalgada by mutableStateOf(false)
    var formInstalacao by mutableStateOf("")

    init {
        carregarAquarios()
    }

    fun carregarAquarios() {
        viewModelScope.launch {
            try {
                listaAquarios = repositorio.buscarAquarios()
            } catch (e: Exception) {
                println("Erro ao buscar dados da API: ${e.message}")
            }
        }
    }

    fun preencherFormulario(aquario: Aquario) {
        formId = aquario.id
        formNome = aquario.nome
        formCapacidade = aquario.capacidadeLitros.toString()
        formAguaSalgada = aquario.aguaSalgada
        formInstalacao = aquario.dataInstalacao
    }

    fun limparCampos() {
        formId = null
        formNome = ""
        formCapacidade = ""
        formAguaSalgada = false
        formInstalacao = ""
    }

    fun gravar() {
        viewModelScope.launch {
            try {
                val aquario = Aquario(
                    id = formId,
                    nome = formNome,
                    capacidadeLitros = formCapacidade.toDoubleOrNull() ?: 0.0,
                    aguaSalgada = formAguaSalgada,
                    dataInstalacao = formInstalacao
                )

                if (formId.isNullOrBlank()) {
                    // Se não tem ID, cria um NOVO registro
                    repositorio.adicionarAquario(aquario)
                } else {
                    // Se tem ID, ATUALIZA o existente
                    repositorio.atualizarAquario(aquario)
                }

                limparCampos()
                carregarAquarios()

            } catch (e: Exception) {
                println("Erro ao gravar na API: ${e.message}")
            }
        }
    }

    fun apagar(id: String?) {
        if (id.isNullOrBlank()) return
        viewModelScope.launch {
            try {
                repositorio.apagarAquario(id)
                carregarAquarios()
            } catch (e: Exception) {
                println("Erro ao apagar na API: ${e.message}")
            }
        }
    }
}
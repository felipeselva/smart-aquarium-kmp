package org.example.project.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.model.Aquario
import org.example.project.repository.AquarioRepositorioRemoto

class AquarioViewModel(private val repositorio: AquarioRepositorioRemoto) : ViewModel() {
    var listaAquarios by mutableStateOf<List<Aquario>>(emptyList())

    var idAtual by mutableStateOf<String?>(null)
    var formNome by mutableStateOf("")
    var formCapacidade by mutableStateOf("")
    var formInstalacao by mutableStateOf("")
    var mensagemErro by mutableStateOf<String?>(null)

    fun carregarAquarios() {
        viewModelScope.launch { listaAquarios = repositorio.obterAquarios() }
    }

    fun preencherFormulario(a: Aquario) {
        idAtual = a.id
        formNome = a.nome
        formCapacidade = a.capacidadeLitros.toString()
        formInstalacao = a.dataInstalacao
    }

    fun limparCampos() {
        idAtual = null
        formNome = ""
        formCapacidade = ""
        formInstalacao = ""
    }

    fun gravar(onSucesso: () -> Unit) {
        val cap = formCapacidade.toDoubleOrNull() ?: 0.0

        val aquario = Aquario(
            id = idAtual,
            nome = formNome,
            capacidadeLitros = cap,
            aguaSalgada = false,
            dataInstalacao = formInstalacao
        )

        viewModelScope.launch {
            if (idAtual == null) {
                repositorio.gravarAquario(aquario)
            } else {
                repositorio.atualizarAquario(aquario)
            }
            carregarAquarios()
            limparCampos()
            onSucesso()
        }
    }

    fun apagar(id: String?) {
        if (id == null) return
        viewModelScope.launch {
            repositorio.excluirAquario(id)
            carregarAquarios()
        }
    }
}
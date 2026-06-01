package org.example.project.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.model.Aquario
import org.example.project.repository.RepositorioRemoto

class AquarioViewModel(private val repositorio: RepositorioRemoto) : ViewModel() {
    var listaAquarios by mutableStateOf<List<Aquario>>(emptyList())

    // Variáveis do formulário (ID agora é String? para bater com o seu Model)
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
        formCapacidade = a.capacidadeLitros.toString() // Corrigido para capacidadeLitros
        formInstalacao = a.dataInstalacao
    }

    fun limparCampos() {
        idAtual = null; formNome = ""; formCapacidade = ""; formInstalacao = ""
    }

    fun gravar() {
        val cap = formCapacidade.toDoubleOrNull() ?: 0.0

        val aquario = Aquario(
            id = idAtual, // Pode ser null ou ter o ID do aquário que estamos a editar
            nome = formNome,
            capacidadeLitros = cap,
            aguaSalgada = false,
            dataInstalacao = formInstalacao
        )

        viewModelScope.launch {
            if (idAtual == null) {
                // Se não tem ID, é um aquário novo!
                repositorio.gravarAquario(aquario)
            } else {
                // Se já tem ID, o utilizador carregou no botão "Editar" antes!
                repositorio.atualizarAquario(aquario)
            }

            // Depois de salvar/atualizar, recarrega a lista e limpa o formulário
            carregarAquarios()
            limparCampos()
        }
    }

    fun apagar(id: String?) { // Agora aceita String? sem reclamar
        if (id == null) return
        viewModelScope.launch {
            repositorio.excluirAquario(id) // Descomente quando o repositório tiver essa função
            carregarAquarios()
        }
    }
}
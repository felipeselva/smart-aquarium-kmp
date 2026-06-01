package org.example.project.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.model.SensorLeitura
import org.example.project.repository.RepositorioRemoto

class SensorLeituraViewModel(private val repositorio: RepositorioRemoto) : ViewModel() {

    var listaSensores by mutableStateOf<List<SensorLeitura>>(emptyList())

    // Variáveis do formulário (ID agora é String)
    var idAtual by mutableStateOf<String?>(null)
    var formIdAquario by mutableStateOf("")
    var formTemperatura by mutableStateOf("")
    var formHoraLeitura by mutableStateOf("")

    fun carregarLeituras() {
        viewModelScope.launch {
            listaSensores = repositorio.obterLeituras() // Ativado!
        }
    }

    fun preencherFormulario(l: SensorLeitura) {
        idAtual = l.id
        formIdAquario = l.idAquario // Já é String no seu Model!
        formTemperatura = l.temperatura.toString()
        formHoraLeitura = l.horaLeitura
    }

    fun limparCampos() {
        idAtual = null; formIdAquario = ""; formTemperatura = ""; formHoraLeitura = ""
    }

    fun gravar() {
        val temp = formTemperatura.toDoubleOrNull() ?: 0.0
        val leitura = SensorLeitura(
            id = idAtual ?: "",
            idAquario = formIdAquario,
            temperatura = temp,
            alertaAtivo = false,
            horaLeitura = formHoraLeitura
        )

        viewModelScope.launch {
            repositorio.gravarLeitura(leitura) // Ativado!
            carregarLeituras() // Recarrega a lista
            limparCampos()
        }
    }

    fun apagar(id: String) {
        viewModelScope.launch {
            repositorio.excluirLeitura(id) // Ativado!
            carregarLeituras()
        }
    }
}
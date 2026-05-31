package org.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.example.project.model.SensorLeitura
import org.example.project.repository.RepositorioRemoto
import kotlin.random.Random

class SensorLeituraViewModel : ViewModel() {

    private val repositorio = RepositorioRemoto()

    var listaSensores by mutableStateOf(listOf<SensorLeitura>())
        private set

    // Campos do formulário mapeados com a sua classe SensorLeitura
    var formId by mutableStateOf("")
    var formIdAquario by mutableStateOf("")
    var formTemperatura by mutableStateOf("")
    var formAlertaAtivo by mutableStateOf(false)
    var formHoraLeitura by mutableStateOf("")

    init {
        carregarSensores()
    }

    fun carregarSensores() {
        listaSensores = repositorio.getLeituras().toList()
    }

    fun preencherFormulario(sensor: SensorLeitura) {
        formId = sensor.id
        formIdAquario = sensor.idAquario
        formTemperatura = sensor.temperatura.toString()
        formAlertaAtivo = sensor.alertaAtivo
        formHoraLeitura = sensor.horaLeitura
    }

    fun limparCampos() {
        formId = ""
        formIdAquario = ""
        formTemperatura = ""
        formAlertaAtivo = false
        formHoraLeitura = ""
    }

    fun gravar() {
        val sensor = SensorLeitura(
            // Agora usamos o Random nativo do Kotlin para gerar um ID que funciona em Android e iOS
            id = formId.ifBlank { Random.nextInt(100000, 999999).toString() },
            idAquario = formIdAquario,
            temperatura = formTemperatura.toDoubleOrNull() ?: 0.0,
            alertaAtivo = formAlertaAtivo,
            horaLeitura = formHoraLeitura
        )

        if (formId.isBlank()) {
            repositorio.addLeitura(sensor)
        } else {
            repositorio.updateLeitura(sensor)
        }

        limparCampos()
        carregarSensores()
    }

    fun apagar(id: String) {
        repositorio.deleteLeitura(id)
        carregarSensores()
    }
}
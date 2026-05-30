package org.example.project.viewmodel

class SensorLeituraViewModel {

    package org.example.project.viewmodel

    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.setValue
    import org.example.project.model.SensorLeitura
    import org.example.project.repository.RepositorioRemoto
    import kotlin.random.Random

    class SensorLeituraViewModel(private val repositorio: RepositorioRemoto) {

        // 1. Variável de estado para guardar a lista de leituras do sensor
        var listaLeituras by mutableStateOf(listOf<SensorLeitura>())
            private set

        // 2. Variáveis de estado para cada campo do formulário
        var formId by mutableStateOf("")
        var formIdAquario by mutableStateOf("")
        var formTemperatura by mutableStateOf("") // String para facilitar no campo de texto
        var formAlertaAtivo by mutableStateOf(false)
        var formHoraLeitura by mutableStateOf("")

        init {
            // Carrega a lista do banco em memória ao iniciar
            carregarLista()
        }

        // ==========================================
        // FUNÇÕES DO CRUD
        // ==========================================

        private fun carregarLista() {
            listaLeituras = repositorio.getLeituras()
        }

        fun gravar() {
            val leitura = SensorLeitura(
                id = formId.ifEmpty { Random.nextInt(1000, 9999).toString() },
                idAquario = formIdAquario,
                temperatura = formTemperatura.toDoubleOrNull() ?: 0.0,
                alertaAtivo = formAlertaAtivo,
                horaLeitura = formHoraLeitura
            )

            // Se não tem ID preenchido, é POST. Se tem, é PUT.
            if (formId.isEmpty()) {
                repositorio.addLeitura(leitura)
            } else {
                repositorio.updateLeitura(leitura)
            }

            limparCampos()
            carregarLista()
        }

        fun apagar(id: String) {
            repositorio.deleteLeitura(id)
            carregarLista()
        }

        // Acionado pelo botão Editar do Card
        fun preencherFormulario(leitura: SensorLeitura) {
            formId = leitura.id
            formIdAquario = leitura.idAquario
            formTemperatura = leitura.temperatura.toString()
            formAlertaAtivo = leitura.alertaAtivo
            formHoraLeitura = leitura.horaLeitura
        }

        fun limparCampos() {
            formId = ""
            formIdAquario = ""
            formTemperatura = ""
            formAlertaAtivo = false
            formHoraLeitura = ""
        }
    }
}
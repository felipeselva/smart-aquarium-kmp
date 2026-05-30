package entidades

class AquarioViewModel {

    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.setValue
    import kotlin.random.Random

    // O ViewModel recebe o RepositorioRemoto para poder usar o nosso banco de dados na memória
    class AquarioViewModel(private val repositorio: RepositorioRemoto) {

        // 1. Variável de estado para guardar a lista de aquários
        var listaAquarios by mutableStateOf(listOf<Aquario>())
            private set // A tela só pode ler a lista, não pode alterar diretamente

        // 2. Variáveis de estado para cada campo do formulário
        var formId by mutableStateOf("")
        var formNome by mutableStateOf("")
        var formCapacidade by mutableStateOf("") // Deixamos como String para facilitar a digitação no campo de texto
        var formAguaSalgada by mutableStateOf(false)
        var formInstalacao by mutableStateOf("")

        init {
            // Assim que o ViewModel nasce, ele carrega a lista do banco
            carregarLista()
        }

        // ==========================================
        // FUNÇÕES DO CRUD (Ações da Tela)
        // ==========================================

        private fun carregarLista() {
            listaAquarios = repositorio.getAquarios()
        }

        fun gravar() {
            // Transforma os dados digitados na tela em um objeto Aquario
            val aquario = Aquario(
                // Se o ID estiver vazio (novo registro), gera um número aleatório. Se não, usa o ID existente (edição)
                id = formId.ifEmpty { Random.nextInt(1000, 9999).toString() },
                nome = formNome,
                capacidadeLitros = formCapacidade.toDoubleOrNull() ?: 0.0,
                aguaSalgada = formAguaSalgada,
                dataInstalacao = formInstalacao
            )

            // Verifica se é um POST (novo) ou PUT (atualização)
            if (formId.isEmpty()) {
                repositorio.addAquario(aquario)
            } else {
                repositorio.updateAquario(aquario)
            }

            limparCampos() // Limpa o formulário após salvar
            carregarLista() // Atualiza a lista da tela
        }

        fun apagar(id: String) {
            repositorio.deleteAquario(id)
            carregarLista() // Recarrega a lista sem o item apagado
        }

        // Usado quando a dupla clicar no ícone de "Editar" lá no Card
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
    }
}
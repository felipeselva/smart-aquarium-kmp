package entidades

import org.example.project.model.Aquario
import org.example.project.model.SensorLeitura

class RepositorioRemoto {
    class RepositorioRemoto {

        // Simulando o nosso "Banco de Dados" na memória para a prova (AV1)
        private val aquariosNoBanco = mutableListOf<Aquario>()
        private val leiturasNoBanco = mutableListOf<SensorLeitura>()

        // ==========================================
        // MÉTODOS HTTP (CRUD) PARA O AQUÁRIO
        // ==========================================

        // GET - Buscar toda a lista
        fun getAquarios(): List<Aquario> {
            return aquariosNoBanco.toList() // Retorna uma cópia da lista
        }

        // POST - Criar um novo
        fun addAquario(aquario: Aquario) {
            aquariosNoBanco.add(aquario)
        }

        // PUT - Atualizar um existente
        fun updateAquario(aquarioAtualizado: Aquario) {
            // Procura se o aquário existe pelo ID
            val index = aquariosNoBanco.indexOfFirst { it.id == aquarioAtualizado.id }
            if (index != -1) {
                aquariosNoBanco[index] = aquarioAtualizado // Substitui pelo novo
            }
        }

        // DELETE - Apagar
        fun deleteAquario(id: String) {
            aquariosNoBanco.removeAll { it.id == id }
        }

        // ==========================================
        // MÉTODOS HTTP (CRUD) PARA O SENSOR
        // ==========================================

        fun getLeituras(): List<SensorLeitura> = leiturasNoBanco.toList()

        fun addLeitura(leitura: SensorLeitura) { leiturasNoBanco.add(leitura) }

        fun updateLeitura(leituraAtualizada: SensorLeitura) {
            val index = leiturasNoBanco.indexOfFirst { it.id == leituraAtualizada.id }
            if (index != -1) leiturasNoBanco[index] = leituraAtualizada
        }

        fun deleteLeitura(id: String) {
            leiturasNoBanco.removeAll { it.id == id }
        }
    }
}
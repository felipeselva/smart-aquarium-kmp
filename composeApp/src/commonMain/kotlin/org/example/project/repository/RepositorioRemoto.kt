package org.example.project.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.model.Aquario
import org.example.project.model.SensorLeitura

class RepositorioRemoto {

    private val cliente = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true // Força o envio da água salgada sempre (resolveu seu erro!)
            })
        }
    }

    // 👇 ATENÇÃO: NÃO ESQUEÇA DE AJUSTAR PARA O IP DO SEU CELULAR 👇
    private val urlBaseAquarios = "http://10.59.113.144:8080/aquarios"

    suspend fun buscarAquarios(): List<Aquario> {
        return cliente.get(urlBaseAquarios).body()
    }

    suspend fun adicionarAquario(aquario: Aquario) {
        cliente.post(urlBaseAquarios) {
            contentType(ContentType.Application.Json)
            setBody(aquario)
        }
    }

    suspend fun atualizarAquario(aquario: Aquario) {
        cliente.put("$urlBaseAquarios/${aquario.id}") {
            contentType(ContentType.Application.Json)
            setBody(aquario)
        }
    }

    suspend fun apagarAquario(id: String) {
        cliente.delete("$urlBaseAquarios/$id")
    }

    // ====== SENSORES (Local) ======
    private val leiturasLocais = mutableListOf<SensorLeitura>()

    fun getLeituras(): List<SensorLeitura> {
        return leiturasLocais
    }

    fun addLeitura(leitura: SensorLeitura) {
        leiturasLocais.add(leitura)
    }

    fun updateLeitura(leitura: SensorLeitura) {
        val index = leiturasLocais.indexOfFirst { it.id == leitura.id }
        if (index != -1) {
            leiturasLocais[index] = leitura
        }
    }

    fun deleteLeitura(id: String) {
        leiturasLocais.removeAll { it.id == id }
    }
}
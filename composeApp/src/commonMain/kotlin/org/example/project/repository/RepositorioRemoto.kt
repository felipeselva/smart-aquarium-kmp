package org.example.project.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.model.Aquario
import org.example.project.model.SensorLeitura

class RepositorioRemoto {

    // ==========================================
    // PARTE 1: KTOR (INTERNET) PARA OS AQUÁRIOS
    // ==========================================
    private val cliente = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Ignora campos a mais que a API mandar
            })
        }
    }

    suspend fun buscarAquarios(): List<Aquario> {
        // 👇👇👇 ATENÇÃO: TROQUE O IP AQUI! 👇👇👇
        // Substitua "192.168.X.X" pelo IPv4 do seu computador (ex: 192.168.0.15)
        return cliente.get("http://192.168.15.6:8080/aquarios").body()
    }

    // ==========================================
    // PARTE 2: DADOS LOCAIS PARA OS SENSORES (Para não dar erro)
    // ==========================================
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
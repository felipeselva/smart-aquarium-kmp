package org.example.project.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.model.SensorLeitura
import org.example.project.model.SessaoUsuario

class SensorRepositorioRemoto(private val sessao: SessaoUsuario) {
    private val BASE_URL = "http://10.92.52.198:8080"
    private val cliente = HttpClient { install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) } }

    suspend fun obterLeituras(): List<SensorLeitura> = try {
        cliente.get("$BASE_URL/sensores") {
            sessao.tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }.body()
    } catch (e: Exception) { emptyList() }

    suspend fun gravarLeitura(leitura: SensorLeitura) = try {
        cliente.post("$BASE_URL/sensores") {
            contentType(ContentType.Application.Json)
            setBody(leitura)
            sessao.tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    } catch (e: Exception) { null }

    suspend fun excluirLeitura(id: String) = try {
        cliente.delete("$BASE_URL/sensores/$id") {
            sessao.tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    } catch (e: Exception) { null }
}
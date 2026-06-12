package org.example.project.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.model.Aquario
import org.example.project.model.SessaoUsuario

class AquarioRepositorioRemoto(private val sessao: SessaoUsuario) {
    private val BASE_URL = "http://10.92.52.198:8080"
    private val cliente = HttpClient { install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) } }

    suspend fun obterAquarios(): List<Aquario> = try {
        cliente.get("$BASE_URL/aquarios") {
            sessao.tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }.body()
    } catch (e: Exception) { emptyList() }

    suspend fun gravarAquario(aquario: Aquario) = try {
        cliente.post("$BASE_URL/aquarios") {
            contentType(ContentType.Application.Json)
            setBody(aquario)
            sessao.tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    } catch (e: Exception) { null }

    suspend fun atualizarAquario(aquario: Aquario) = try {
        cliente.put("$BASE_URL/aquarios/${aquario.id}") {
            contentType(ContentType.Application.Json)
            setBody(aquario)
            sessao.tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    } catch (e: Exception) { null }

    suspend fun excluirAquario(id: String) = try {
        cliente.delete("$BASE_URL/aquarios/$id") {
            sessao.tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    } catch (e: Exception) { null }
}
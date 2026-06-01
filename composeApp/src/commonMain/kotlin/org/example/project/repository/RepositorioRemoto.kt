package org.example.project.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.example.project.model.*

// 🔥 NOVO: Modelo de dados para o envio do Registo
@Serializable
data class CadastroRequest(val nome: String, val email: String, val senha: String)

class RepositorioRemoto {

    var tokenJwt: String? = null
    private val BASE_URL = "http://10.92.52.198:8080"
    private val cliente = HttpClient {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    }

    suspend fun fazerLogin(email: String, senha: String): Boolean {
        return try {
            val res: LoginResponse = cliente.post("$BASE_URL/auth/signin") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, senha))
            }.body()
            tokenJwt = res.token
            true
        } catch (e: Exception) { false }
    }

    // 🔥 NOVA FUNÇÃO: Envia os dados para o Spring Boot criar a conta
    suspend fun cadastrarUsuario(nome: String, email: String, senha: String): Boolean {
        return try {
            // ATENÇÃO: Assumi que a sua rota no Spring Boot é /auth/signup.
            // Se for diferente (ex: /auth/register ou /usuarios), mude a palavra abaixo!
            val response = cliente.post("$BASE_URL/auth/signup") {
                contentType(ContentType.Application.Json)
                setBody(CadastroRequest(nome, email, senha))
            }
            // Se o servidor responder com OK (200) ou Created (201), foi sucesso!
            response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created
        } catch (e: Exception) {
            false
        }
    }

    suspend fun obterAquarios(): List<Aquario> = try {
        cliente.get("$BASE_URL/aquarios") {
            tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }.body()
    } catch (e: Exception) { emptyList() }

    suspend fun gravarAquario(aquario: Aquario) = try {
        cliente.post("$BASE_URL/aquarios") {
            contentType(ContentType.Application.Json)
            setBody(aquario)
            tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    } catch (e: Exception) { null }

    suspend fun atualizarAquario(aquario: Aquario) = try {
        cliente.put("$BASE_URL/aquarios/${aquario.id}") {
            contentType(ContentType.Application.Json)
            setBody(aquario)
            tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    } catch (e: Exception) { null }

    suspend fun excluirAquario(id: String) = try {
        cliente.delete("$BASE_URL/aquarios/$id") {
            tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    } catch (e: Exception) { null }

    // ==========================================
    // FUNÇÕES DOS SENSORES (IoT)
    // ==========================================

    suspend fun obterLeituras(): List<SensorLeitura> = try {
        cliente.get("$BASE_URL/sensores") {
            tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }.body()
    } catch (e: Exception) { emptyList() }

    suspend fun gravarLeitura(leitura: SensorLeitura) = try {
        cliente.post("$BASE_URL/sensores") {
            contentType(ContentType.Application.Json)
            setBody(leitura)
            tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    } catch (e: Exception) { null }

    suspend fun excluirLeitura(id: String) = try {
        cliente.delete("$BASE_URL/sensores/$id") {
            tokenJwt?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    } catch (e: Exception) { null }
}
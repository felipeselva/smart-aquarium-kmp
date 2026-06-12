package org.example.project.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.project.model.CadastroRequest
import org.example.project.model.LoginRequest
import org.example.project.model.LoginResponse
import org.example.project.model.SessaoUsuario

class AuthRepositorioRemoto(private val sessao: SessaoUsuario) {
    private val BASE_URL = "http://10.92.52.198:8080"
    private val cliente = HttpClient { install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) } }

    suspend fun fazerLogin(email: String, senha: String): Boolean = try {
        val res: LoginResponse = cliente.post("$BASE_URL/login") { // Atualizado para a nova rota
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, senha))
        }.body()
        sessao.tokenJwt = res.token
        true
    } catch (e: Exception) { false }

    suspend fun cadastrarUsuario(nome: String, email: String, senha: String): Boolean = try {
        val response = cliente.post("$BASE_URL/register") { // Atualizado para a nova rota!
            contentType(ContentType.Application.Json)
            setBody(CadastroRequest(nome, email, senha))
        }
        response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created
    } catch (e: Exception) { false }
}
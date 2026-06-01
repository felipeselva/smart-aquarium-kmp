package org.example.project.model

import kotlinx.serialization.Serializable

// O pacote que enviamos para o servidor (E-mail e Senha)
@Serializable
data class LoginRequest(
    val email: String,
    val senha: String
)

// O pacote que o servidor nos devolve (O Token JWT)
@Serializable
data class LoginResponse(
    val token: String
)
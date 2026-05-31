package org.example.project.model

import kotlinx.serialization.Serializable

@Serializable
data class Aquario(
    val id: String? = null,
    val nome: String = "",
    val capacidadeLitros: Double = 0.0,
    val aguaSalgada: Boolean = false,
    val dataInstalacao: String = ""
)
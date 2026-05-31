package org.example.project.model

import kotlinx.serialization.Serializable

@Serializable
data class SensorLeitura(
    val id: String = "",
    val idAquario: String = "",
    val temperatura: Double = 0.0,
    val alertaAtivo: Boolean = false,
    val horaLeitura: String = ""
)
package entidades

data class SensorLeitura(
    val id: String = "",
    val idAquario: String = "",
    val temperatura: Double = 0.0,
    val alertaAtivo: Boolean = false,
    val horaLeitura: String = ""
)
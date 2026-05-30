package entidades

data class Aquario(
    val id: String = "",
    val nome: String = "",
    val capacidadeLitros: Double = 0.0,
    val aguaSalgada: Boolean = false,
    val dataInstalacao: String = ""
)
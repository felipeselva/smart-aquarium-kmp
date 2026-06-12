package org.example.project.model

    // Objeto de sessão injetável para compartilhar o token JWT entre os repositórios
    class SessaoUsuario {
        var tokenJwt: String? = null
    }

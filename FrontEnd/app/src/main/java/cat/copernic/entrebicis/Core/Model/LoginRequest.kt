package cat.copernic.entrebicis.Core.Model

/**
 * Classe que representa una sol·licitud d'inici de sessió.
 *
 * Aquesta classe encapsula les credencials necessàries per a l'autenticació d'un usuari.
 * És utilitzada habitualment per enviar dades al backend a través d'una API REST.
 *
 * @property email El correu electrònic de l'usuari per a l'inici de sessió.
 * @property password La contrasenya associada al compte de l'usuari.
 */
data class LoginRequest(
    val email: String,
    val password: String
)

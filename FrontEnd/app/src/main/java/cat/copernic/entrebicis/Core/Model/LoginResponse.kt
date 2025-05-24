package cat.copernic.entrebicis.Core.Model

/**
 * Classe que representa una resposta d'inici de sessió.
 *
 * Aquesta classe encapsula les dades retornades pel servidor després d'un intent d'autenticació exitós.
 * Habitualment s'utilitza per identificar l'usuari que ha iniciat sessió o per obtenir més informació si cal.
 *
 * @property email El correu electrònic de l'usuari que ha iniciat sessió.
 */
data class LoginResponse(
    val email: String,
)

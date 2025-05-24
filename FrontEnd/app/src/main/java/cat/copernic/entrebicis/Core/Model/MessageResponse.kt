package cat.copernic.entrebicis.Core.Model

/**
 * Classe que representa una resposta amb un missatge simple.
 *
 * Aquesta classe és útil per encapsular respostes del servidor que contenen només un missatge textual,
 * com per exemple confirmacions d'accions, errors o altres notificacions.
 *
 * @property message El missatge retornat pel servidor.
 */
data class MessageResponse(
    val message: String
)

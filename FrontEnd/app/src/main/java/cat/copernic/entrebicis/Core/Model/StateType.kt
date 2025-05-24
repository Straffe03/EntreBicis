package cat.copernic.entrebicis.Core.Model

/**
 * Enumeració que representa els diferents estats que pot tenir una recompensa.
 *
 * - **DISPONIBLE**: La recompensa està disponible per ser reservada per un usuari.
 * - **RESERVADA**: La recompensa ha estat reservada per un usuari però encara no ha estat assignada.
 * - **ASSIGNADA**: La recompensa ha estat assignada a un usuari i està preparada per ser recollida.
 * - **RECOLLIDA**: La recompensa ja ha estat recollida pel seu destinatari.
 */
enum class StateType {
    DISPONIBLE,
    RESERVADA,
    ASSIGNADA,
    RECOLLIDA
}

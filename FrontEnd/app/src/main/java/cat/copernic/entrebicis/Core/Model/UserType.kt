package cat.copernic.entrebicis.Core.Model

/**
 * Enum que representa els diferents rols d'usuari dins del sistema EntreBicis.
 *
 * - [ADMIN]: Usuari amb privilegis d'administració que pot gestionar tot el sistema.
 * - [CICLISTA]: Usuari regular que participa a les rutes i acumula punts.
 */
enum class UserType {
    ADMIN,
    CICLISTA
}

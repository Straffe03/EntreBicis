package cat.copernic.entrebicis.Core.Model

/**
 * Representa els possibles estats d'una ruta dins de l'aplicació.
 *
 * Els estats defineixen en quin punt del seu cicle de vida es troba la ruta:
 *
 * - **PENDENT**: La ruta està en procés, s'estan enregistrant punts i encara no ha finalitzat.
 * - **FINALITZADA**: La ruta ha estat completada, no s'hi poden afegir més punts i pot ser validada.
 */
enum class RouteState {
    PENDENT,
    FINALITZADA
}

package cat.copernic.entrebicis.Core.Model

/**
 * Classe de dades que representa els paràmetres globals de configuració del sistema.
 *
 * Aquests paràmetres controlen el comportament de la validació de rutes i el sistema de recompenses.
 *
 * @property id Identificador únic dels paràmetres del sistema.
 * @property maxVelocity Velocitat màxima permesa per validar una ruta (km/h).
 * @property maxStopTime Temps màxim d'aturada (en minuts) abans de considerar una ruta finalitzada automàticament.
 * @property pointsPerKm Nombre de punts obtinguts per cada quilòmetre recorregut.
 * @property rewardExpirationTime Temps de caducitat d'una recompensa (en hores).
 */
data class SystemParameter(
    val id: Long,
    val maxVelocity: Int,
    val maxStopTime: Int,
    val pointsPerKm: Int,
    val rewardExpirationTime: Int
)

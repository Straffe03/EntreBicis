package cat.copernic.entrebicis.Core.Model

/**
 * Representa un punt GPS registrat durant una ruta ciclista.
 *
 * Cada punt GPS conté la seva ubicació geogràfica i el moment exacte en què es va enregistrar.
 * Aquests punts es poden utilitzar per reconstruir la ruta d'un ciclista.
 *
 * @property id Identificador únic del punt GPS.
 * @property latitude Latitud del punt GPS en graus decimals.
 * @property longitude Longitud del punt GPS en graus decimals.
 * @property timestamp Data i hora en què es va registrar el punt.
 * @property route Ruta associada a aquest punt GPS, si escau.
 */
data class GPSPoint(
    val id: Long?,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String,
    var route: Route?
)

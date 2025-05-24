package cat.copernic.entrebicis.Core.Model

/**
 * Representa una ruta enregistrada per un usuari en l'aplicació.
 *
 * Una ruta està formada per múltiples punts GPS i pot tenir diferents estats.
 * També es poden calcular paràmetres com la distància total, la velocitat mitjana
 * i els punts generats en funció de la distància recorreguda.
 *
 * @property id Identificador únic de la ruta.
 * @property state Estat actual de la ruta (PENDENT o FINALITZADA).
 * @property validation Indica si la ruta ha estat validada correctament per un administrador.
 * @property date Data en què s'ha enregistrat la ruta.
 * @property distance Distància total recorreguda en quilòmetres.
 * @property velocity Velocitat mitjana durant la ruta en km/h.
 * @property maxVelocity Velocitat màxima assolida durant la ruta.
 * @property time Temps total en minuts o hores que ha durat la ruta.
 * @property points Punts generats segons la distància recorreguda (habitualment 1 km = 1 punt).
 * @property gpsPoints Llista de punts GPS que conformen la trajectòria de la ruta.
 * @property user Usuari que ha enregistrat la ruta.
 */
data class Route(
    val id: Long?,
    var state: RouteState,
    val validation: Boolean,
    val date: String?,
    var distance: Double,
    var velocity: Double,
    var maxVelocity: Double,
    var time: Double,
    var points: Double,
    val gpsPoints: List<GPSPoint>?,
    var user: User
)

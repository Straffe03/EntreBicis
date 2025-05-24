package MarioOlaya.Projecte4.EntreBicis.entity

import cat.copernic.entrebicis.Core.Model.StateType
import cat.copernic.entrebicis.Core.Model.User

/**
 * Representa una recompensa dins del sistema.
 * Aquesta classe conté informació sobre la recompensa, incloent-hi la seva
 * imatge, nom, nom de la botiga, observacions, descripció, punts necessaris,
 * adreça i estat.
 *
 * @property id Identificador únic de la recompensa.
 * @property image Imatge associada a la recompensa, codificada en Base64.
 * @property name Nom de la recompensa.
 * @property storeName Nom de la botiga que ofereix la recompensa.
 * @property observations Observacions addicionals sobre la recompensa.
 * @property description Descripció detallada de la recompensa.
 * @property points Punts necessaris per obtenir la recompensa.
 * @property date Data de creació de la recompensa.
 * @property reservationDate Data en què s'ha reservat la recompensa.
 * @property assignationDate Data en què s'ha assignat la recompensa a un usuari.
 * @property pickupDate Data de recollida de la recompensa per part de l'usuari.
 * @property adress Adreça física de la botiga on es pot recollir la recompensa.
 * @property state Estat actual de la recompensa (DISPONIBLE, RESERVADA, ASSIGNADA, etc.).
 * @property user Usuari al qual està associada la recompensa, si n'hi ha.
 * 
 * @author Mario Olaya
 */
data class Reward(
    val id: Long?,
    val image: String? = null,
    val name: String,
    val storeName: String,
    val observations: String,
    val description: String,
    val points: Double,
    val date: String?,
    val reservationDate: String?,
    val assignationDate: String?,
    val pickupDate: String?,
    val adress: String,
    var state: StateType,
    var user: User?
)

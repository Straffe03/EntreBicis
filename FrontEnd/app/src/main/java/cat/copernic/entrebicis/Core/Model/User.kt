package cat.copernic.entrebicis.Core.Model

/**
 * Classe que representa un usuari dins del sistema EntreBicis.
 *
 * Aquesta classe encapsula les dades personals i de configuració d'un usuari.
 *
 * @property email Correu electrònic de l'usuari, utilitzat com a identificador únic.
 * @property password Contrasenya de l'usuari (emmagatzemada de forma segura).
 * @property name Nom de l'usuari.
 * @property surname Cognom de l'usuari.
 * @property phone Número de telèfon de contacte de l'usuari.
 * @property signupDate Data d'inscripció de l'usuari al sistema.
 * @property observations Observacions addicionals sobre l'usuari (opcional).
 * @property userType Tipus d'usuari (per exemple, administrador o ciclista).
 * @property city Ciutat de residència de l'usuari.
 * @property points Quantitat de punts acumulats per l'usuari.
 * @property image Imatge de perfil de l'usuari codificada en Base64 (opcional).
 */
data class User(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val phone: String,
    val signupDate: String,
    val observations: String?,
    val userType: UserType,
    val city: String,
    val points: Double,
    val image: String? = null
)

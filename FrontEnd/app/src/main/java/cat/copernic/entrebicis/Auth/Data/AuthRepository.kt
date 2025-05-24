package cat.copernic.entrebicis.Rutas.Data

import cat.copernic.entrebicis.Core.RetrofitInstance
import retrofit2.Response

/**
 * Repositori per gestionar les operacions de la API REST d'autenticació.
 *
 * Aquesta classe encapsula la crida a l'API REST relacionada amb la gestió
 * de contrasenyes, facilitant la seva reutilització i manteniment.
 */
class AuthRepository {

    /**
     * Envia una petició per iniciar el procés de recuperació de contrasenya
     * associada al correu electrònic indicat.
     *
     * @param email Correu electrònic de l'usuari que vol recuperar la seva contrasenya.
     * @return Un objecte Response que indica el resultat de la crida a l'API.
     */
    suspend fun recoverPassword(email: String): Response<Void> {
        return RetrofitInstance.authApi.recoverPassword(email)
    }
}

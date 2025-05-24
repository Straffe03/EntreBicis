package cat.copernic.entrebicis.Rutas.Data

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Interfície per a la comunicació amb l'API d'autenticació.
 *
 * Aquesta interfície defineix les operacions HTTP relacionades amb l'autenticació
 * de l'usuari, com per exemple la recuperació de contrasenya.
 */
interface AuthApiRest {

    /**
     * Envia una petició per recuperar la contrasenya d'un usuari a través del seu correu electrònic.
     *
     * @param email Correu electrònic de l'usuari que ha sol·licitat recuperar la contrasenya.
     * @return Una resposta HTTP sense cos (Void) que indica si la petició s'ha processat correctament.
     */
    @POST("auth/recover")
    suspend fun recoverPassword(@Query("email") email: String): Response<Void>
}

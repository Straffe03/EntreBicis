package cat.copernic.entrebicis.Users.Data

import android.util.Log
import cat.copernic.entrebicis.Core.Model.LoginRequest
import cat.copernic.entrebicis.Core.Model.LoginResponse
import cat.copernic.entrebicis.Core.Model.SystemParameter
import cat.copernic.entrebicis.Core.Model.User
import cat.copernic.entrebicis.Core.RetrofitInstance
import retrofit2.Response

/**
 * Repositori per gestionar les operacions de la API REST d'usuaris.
 */
class UserRepository {

    companion object {
        private const val TAG = "UserRepository"
    }

    /**
     * Verifica les credencials d'inici de sessió.
     *
     * @param email El correu electrònic de l'usuari.
     * @param password La contrasenya.
     * @return La resposta de la API amb la informació de l'usuari en cas d'èxit.
     */
    suspend fun loginUser(email: String, password: String): Response<LoginResponse> {
        Log.d(TAG, "S'està intentant iniciar sessió per l'usuari: $email")
        val request = LoginRequest(email, password)
        return RetrofitInstance.api.loginUser(request)
    }

    /**
     * Obté un usuari pel seu correu electrònic.
     *
     * @param email El correu electrònic de l'usuari.
     * @return La resposta de la API amb l'usuari obtingut.
     */
    suspend fun getUser(email: String): Response<User> {
        Log.d(TAG, "S'està sol·licitant l'usuari amb email: $email")
        return RetrofitInstance.api.getUser(email)
    }

    /**
     * Actualitza les dades d'un usuari.
     *
     * @param user L'usuari amb les dades a modificar.
     * @return La resposta de la API amb l'usuari actualitzat.
     */
    suspend fun updateUser(user: User): Response<User> {
        Log.d(TAG, "S'està actualitzant l'usuari amb email: ${user.email}")
        return RetrofitInstance.api.updateUser(user)
    }

    /**
     * Obté els paràmetres del sistema.
     *
     * @return La resposta de la API amb els paràmetres del sistema.
     */
    suspend fun getParameters(): Response<SystemParameter> {
        Log.d(TAG, "S'estan obtenint els paràmetres del sistema.")
        return RetrofitInstance.api.getParameters()
    }
}

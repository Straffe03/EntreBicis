package cat.copernic.entrebicis.Users.Domain

import android.util.Log
import cat.copernic.entrebicis.Core.Model.User
import cat.copernic.entrebicis.Users.Data.UserRepository

/**
 * Classe que encapsula els casos d'ús per a les operacions d'usuaris.
 *
 * @param repository El repositori d'usuaris.
 */
class UseCases(private val repository: UserRepository) {

    companion object {
        private const val TAG = "UseCases"
    }

    /**
     * Verifica les credencials d'inici de sessió.
     *
     * @param email El correu electrònic de l'usuari.
     * @param password La contrasenya.
     * @return La resposta de la API amb la informació de l'usuari en cas d'èxit.
     */
    suspend fun loginUser(email: String, password: String) = repository.loginUser(email, password).also {
        Log.d(TAG, "S'ha sol·licitat la verificació de login per a: $email")
    }

    /**
     * Obté un usuari pel seu correu electrònic.
     *
     * @param email El correu electrònic de l'usuari.
     * @return La resposta de la API amb l'usuari corresponent.
     */
    suspend fun getUser(email: String) = repository.getUser(email).also {
        Log.d(TAG, "S'ha sol·licitat l'usuari amb email: $email")
    }

    /**
     * Actualitza les dades d'un usuari.
     *
     * @param user L'usuari amb les dades a modificar.
     * @return La resposta de la API amb l'usuari actualitzat.
     */
    suspend fun updateUser(user: User) = repository.updateUser(user).also {
        Log.d(TAG, "S'ha sol·licitat l'actualització de l'usuari amb email: ${user.email}")
    }

    /**
     * Obté els paràmetres del sistema.
     *
     * @return La resposta de la API amb els paràmetres configurables del sistema.
     */
    suspend fun getParameters() = repository.getParameters().also {
        Log.d(TAG, "S'han sol·licitat els paràmetres del sistema.")
    }
}

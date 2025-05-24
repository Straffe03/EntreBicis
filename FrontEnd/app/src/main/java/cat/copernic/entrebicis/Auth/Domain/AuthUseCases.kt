package cat.copernic.entrebicis.Auth.Domain

import cat.copernic.entrebicis.Rutas.Data.AuthRepository

/**
 * Classe que encapsula els casos d'ús relacionats amb l'autenticació.
 *
 * Aquesta classe actua com una capa de domini entre el repositori i la interfície d'usuari.
 * És responsable de coordinar les operacions específiques relacionades amb la recuperació
 * de contrasenyes.
 *
 * @param repository El repositori encarregat de les operacions d'autenticació.
 */
class AuthUseCases(private val repository: AuthRepository) {

    /**
     * Inicia el procés de recuperació de contrasenya per a l'usuari especificat.
     *
     * @param email El correu electrònic de l'usuari que sol·licita la recuperació.
     * @return El resultat de la crida a l'API REST com a Response<Void>.
     */
    suspend fun recoverPassword(email: String) = repository.recoverPassword(email)
}

package cat.copernic.entrebicis.Users.Data

import cat.copernic.entrebicis.Core.Model.LoginRequest
import cat.copernic.entrebicis.Core.Model.LoginResponse
import cat.copernic.entrebicis.Core.Model.SystemParameter
import cat.copernic.entrebicis.Core.Model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interfície per a les operacions de la API REST d'usuaris.
 * Permet verificar credencials, obtenir informació d'usuaris, actualitzar dades i consultar paràmetres de sistema.
 */
interface UserApiRest {

    /**
     * Verifica les credencials d'inici de sessió.
     *
     * @param credentials Les credencials d'inici de sessió.
     * @return La resposta de la API amb la informació de l'usuari en format [LoginResponse].
     */
    @POST("login/verify")
    suspend fun loginUser(@Body credentials: LoginRequest): Response<LoginResponse>

    /**
     * Obté un usuari pel seu ID.
     *
     * @param userId L'identificador únic de l'usuari.
     * @return La resposta de la API amb l'usuari obtingut.
     */
    @GET("user/view/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<User>

    /**
     * Actualitza un usuari.
     *
     * @param user L'usuari amb les dades modificades.
     * @return La resposta de la API amb l'usuari actualitzat.
     */
    @PUT("user/update/{userId}")
    suspend fun updateUser(@Body user: User): Response<User>

    /**
     * Obté els paràmetres de sistema.
     *
     * @return La resposta de la API amb els paràmetres del sistema.
     */
    @GET("parameters/get")
    suspend fun getParameters(): Response<SystemParameter>
}

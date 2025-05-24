package cat.copernic.entrebicis.Reward.Data

import MarioOlaya.Projecte4.EntreBicis.entity.Reward
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interfície per a definir les operacions disponibles a la API REST de recompenses.
 * Aquesta interfície permet accedir, consultar i modificar recompenses mitjançant Retrofit.
 */
interface RewardApiRest {

    /**
     * Obté totes les recompenses disponibles.
     *
     * @return Una resposta HTTP que conté una llista d'objectes [Reward].
     */
    @GET("reward/list")
    suspend fun getAllRewards(): Response<List<Reward>>

    /**
     * Obté la informació d'una recompensa específica a partir del seu identificador.
     *
     * @param id Identificador únic de la recompensa a visualitzar.
     * @return Una resposta HTTP que conté l'objecte [Reward] corresponent.
     */
    @GET("reward/view/{id}")
    suspend fun getReward(@Path("id") id: Long): Response<Reward>

    /**
     * Obté totes les recompenses associades a un usuari identificat per l'email.
     *
     * @param email Correu electrònic de l'usuari del qual es volen obtenir les recompenses.
     * @return Una resposta HTTP amb una llista de recompenses de l'usuari.
     */
    @GET("reward/list/{email}")
    suspend fun getRewardsByUser(@Path("email") email: String): Response<List<Reward>>

    /**
     * Actualitza la informació d'una recompensa existent.
     *
     * @param reward Objecte [Reward] amb les dades actualitzades.
     * @return Una resposta HTTP que conté l'objecte [Reward] ja modificat.
     */
    @PUT("reward/update")
    suspend fun updateReward(@Body reward: Reward): Response<Reward>
}

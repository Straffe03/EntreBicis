package cat.copernic.entrebicis.Reward.Data

import MarioOlaya.Projecte4.EntreBicis.entity.Reward
import cat.copernic.entrebicis.Core.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Response
import android.util.Log

/**
 * Repositori per gestionar les operacions relacionades amb recompenses mitjançant la API REST.
 */
class RewardRepository {

    companion object {
        private const val TAG = "RewardRepository"
    }

    /**
     * Obté totes les recompenses disponibles.
     *
     * @return Una resposta HTTP amb una llista d'objectes [Reward].
     */
    suspend fun getAllRewards(): Response<List<Reward>> {
        Log.d(TAG, "S'estan obtenint totes les recompenses des del servidor...")
        return RetrofitInstance.rewardApi.getAllRewards()
    }

    /**
     * Obté una recompensa específica a partir del seu identificador.
     *
     * @param id Identificador únic de la recompensa.
     * @return Una resposta HTTP amb l'objecte [Reward] corresponent.
     */
    suspend fun getReward(id: Long): Response<Reward> {
        Log.d(TAG, "S'està obtenint la recompensa amb ID: $id")
        return RetrofitInstance.rewardApi.getReward(id)
    }

    /**
     * Obté totes les recompenses associades a un usuari mitjançant el seu correu electrònic.
     *
     * @param email Correu electrònic de l'usuari.
     * @return Una resposta HTTP amb una llista de recompenses de l'usuari.
     */
    suspend fun getRewardsByUser(email: String): Response<List<Reward>> {
        Log.d(TAG, "S'estan obtenint les recompenses per a l'usuari amb email: $email")
        return RetrofitInstance.rewardApi.getRewardsByUser(email)
    }

    /**
     * Actualitza les dades d'una recompensa existent.
     *
     * @param reward Objecte [Reward] amb la informació actualitzada.
     * @return Una resposta HTTP amb la recompensa modificada.
     */
    suspend fun updateReward(reward: Reward): Response<Reward> {
        Log.d(TAG, "S'està actualitzant la recompensa amb ID: ${reward.id}")
        return RetrofitInstance.rewardApi.updateReward(reward)
    }
}

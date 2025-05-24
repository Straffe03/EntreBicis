package cat.copernic.entrebicis.Reward.Domain

import MarioOlaya.Projecte4.EntreBicis.entity.Reward
import cat.copernic.entrebicis.Reward.Data.RewardRepository
import android.util.Log

/**
 * Classe que encapsula els casos d'ús per a les operacions de recompenses.
 *
 * @param repository El repositori de recompenses utilitzat per accedir a les dades.
 */
class RewardUseCases(private val repository: RewardRepository) {

    companion object {
        private const val TAG = "RewardUseCases"
    }

    /**
     * Obté totes les recompenses disponibles.
     *
     * @return La resposta HTTP amb la llista de recompenses.
     */
    suspend fun listAllRewards() = repository.getAllRewards().also {
        Log.d(TAG, "S'han sol·licitat totes les recompenses.")
    }

    /**
     * Obté una recompensa específica mitjançant el seu identificador.
     *
     * @param id Identificador únic de la recompensa.
     * @return La resposta HTTP amb la recompensa corresponent.
     */
    suspend fun getReward(id: Long) = repository.getReward(id).also {
        Log.d(TAG, "S'ha sol·licitat la recompensa amb ID: $id")
    }

    /**
     * Obté totes les recompenses associades a un usuari.
     *
     * @param email Correu electrònic de l'usuari.
     * @return La resposta HTTP amb la llista de recompenses de l'usuari.
     */
    suspend fun getRewardsByUser(email: String) = repository.getRewardsByUser(email).also {
        Log.d(TAG, "S'han sol·licitat les recompenses de l'usuari amb email: $email")
    }

    /**
     * Actualitza una recompensa existent.
     *
     * @param reward Objecte [Reward] amb les dades actualitzades.
     * @return La resposta HTTP amb la recompensa actualitzada.
     */
    suspend fun updateReward(reward: Reward) = repository.updateReward(reward).also {
        Log.d(TAG, "S'ha sol·licitat l'actualització de la recompensa amb ID: ${reward.id}")
    }
}

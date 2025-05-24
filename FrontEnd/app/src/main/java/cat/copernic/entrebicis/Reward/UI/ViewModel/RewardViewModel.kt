package cat.copernic.entrebicis.Reward.UI.ViewModel

import MarioOlaya.Projecte4.EntreBicis.entity.Reward
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.entrebicis.Core.Model.StateType
import cat.copernic.entrebicis.Core.Model.User
import cat.copernic.entrebicis.Reward.Domain.RewardUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel per a la gestió de recompenses.
 * Controla la lògica de negoci i actualitza l'estat de la UI mitjançant StateFlow.
 *
 * @param rewardUseCases Casos d'ús relacionats amb recompenses.
 */
class RewardViewModel(private val rewardUseCases: RewardUseCases) : ViewModel() {

    private val _rewards = MutableStateFlow<List<Reward>>(emptyList())
    val rewards: StateFlow<List<Reward>> = _rewards

    private val _errorMsg = MutableStateFlow<String?>("Error Desconegut")
    val errorMsg: StateFlow<String?> = _errorMsg

    val _reward = MutableStateFlow<Reward?>(null)
    val reward: StateFlow<Reward?> = _reward

    var _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    /**
     * Llista totes les recompenses disponibles des del servidor.
     */
    fun listRewards() {
        viewModelScope.launch {
            try {
                Log.d("RewardViewModel", "S'estan obtenint totes les recompenses.")
                val response = rewardUseCases.listAllRewards()
                if (response.isSuccessful) {
                    response.body()?.let { rewardList ->
                        _rewards.value = rewardList
                        Log.d("RewardViewModel", "Recompenses carregades correctament.")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error desconegut del servidor"
                    Log.e("RewardViewModel", "Error API: $errorMessage")
                    _errorMsg.value = errorMessage
                }
            } catch (e: Exception) {
                Log.e("RewardViewModel", "Error obtenint recompenses: ${e.message}")
            }
        }
    }

    /**
     * Llista totes les recompenses associades a un usuari concret.
     *
     * @param email Correu electrònic de l'usuari.
     */
    fun listRewardsByUser(email: String) {
        viewModelScope.launch {
            try {
                Log.d("RewardViewModel", "S'estan obtenint recompenses per l'usuari: $email")
                val response = rewardUseCases.getRewardsByUser(email)
                if (response.isSuccessful) {
                    response.body()?.let { rewardList ->
                        _rewards.value = rewardList
                        Log.d("RewardViewModel", "Recompenses de l'usuari carregades correctament.")
                    }
                } else {
                    Log.e("RewardViewModel", "Error API: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("RewardViewModel", "Error obtenint recompenses per usuari: ${e.message}")
            }
        }
    }

    /**
     * Obté una recompensa específica pel seu identificador.
     *
     * @param id Identificador de la recompensa.
     */
    fun getReward(id: Long) {
        viewModelScope.launch {
            try {
                Log.d("RewardViewModel", "S'està obtenint la recompensa amb ID: $id")
                val response = rewardUseCases.getReward(id)
                Log.d("RewardViewModel", "Resposta: ${response.body()}")
                if (response.isSuccessful) {
                    _reward.value = response.body()
                } else {
                    Log.e("RewardViewModel", "Error API: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("RewardViewModel", "Error obtenint recompensa amb ID $id: ${e.message}")
            }
        }
    }

    /**
     * Reserva una recompensa per a un usuari.
     *
     * @param reward Recompensa a reservar.
     * @param currentUser Usuari actual.
     */
    fun reservar(reward: Reward, currentUser: User) {
        reward.state = StateType.RESERVADA
        reward.user = currentUser
        Log.d("RewardViewModel", "Reserva de recompensa amb ID: ${reward.id}")
        updateReward(reward)
    }

    /**
     * Marca una recompensa com recollida per un usuari.
     *
     * @param reward Recompensa a recollir.
     * @param currentUser Usuari actual.
     */
    fun recollir(reward: Reward, currentUser: User) {
        reward.state = StateType.RECOLLIDA
        reward.user = currentUser
        Log.d("RewardViewModel", "Recollida de recompensa amb ID: ${reward.id}")
        updateReward(reward)
    }

    /**
     * Actualitza una recompensa existent.
     *
     * @param reward Recompensa a actualitzar.
     */
    fun updateReward(reward: Reward) {
        viewModelScope.launch {
            try {
                Log.d("RewardViewModel", "Actualitzant recompensa amb ID: ${reward.id}")
                val response = rewardUseCases.updateReward(reward)
                _updateSuccess.value = response.isSuccessful
                if (response.isSuccessful) {
                    _reward.value = response.body()
                    Log.d("RewardViewModel", "Recompensa actualitzada correctament.")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error desconegut del servidor"
                    _errorMsg.value = errorMessage
                    Log.e("RewardViewModel", "Error actualitzant recompensa: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e("RewardViewModel", "Error actualitzant recompensa amb ID ${reward.id}: ${e.message}")
            }
        }
    }
}

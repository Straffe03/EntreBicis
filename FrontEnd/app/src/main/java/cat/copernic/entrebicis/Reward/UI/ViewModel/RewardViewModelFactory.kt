package cat.copernic.entrebicis.Reward.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.entrebicis.Reward.Domain.RewardUseCases

/**
 * Factory per crear instàncies de [RewardViewModel].
 *
 * @param rewardUseCases Casos d'ús associats a les operacions de recompenses.
 */
class RewardViewModelFactory(private val rewardUseCases: RewardUseCases) : ViewModelProvider.Factory {

    /**
     * Crea una nova instància de [RewardViewModel].
     *
     * @param modelClass La classe del ViewModel a instanciar.
     * @return Una nova instància de [RewardViewModel].
     * @throws IllegalArgumentException Si la classe proporcionada no és compatible amb [RewardViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RewardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RewardViewModel(rewardUseCases) as T
        }
        throw IllegalArgumentException("Viewmodel not found")
    }
}

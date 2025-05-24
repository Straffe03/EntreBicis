package cat.copernic.entrebicis.Users.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.entrebicis.Users.Domain.UseCases

/**
 * Factory per crear instàncies de [UserViewModel].
 *
 * @param useCases Els casos d'ús per a les operacions d'usuaris.
 */
class UserViewModelFactory(private val useCases: UseCases) : ViewModelProvider.Factory {

    /**
     * Crea una nova instància de [UserViewModel].
     *
     * @param modelClass La classe del ViewModel a crear.
     * @return Una nova instància de [UserViewModel].
     * @throws IllegalArgumentException Si la classe no és assignable a [UserViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(useCases) as T
        }
        throw IllegalArgumentException("Viewmodel not found")
    }
}

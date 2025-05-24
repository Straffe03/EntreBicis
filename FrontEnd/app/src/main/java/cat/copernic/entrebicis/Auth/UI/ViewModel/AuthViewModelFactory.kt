package cat.copernic.entrebicis.Rutas.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.entrebicis.Auth.Domain.AuthUseCases
import cat.copernic.entrebicis.route.ui.viewmodel.AuthViewModel

/**
 * Fàbrica per crear instàncies del ViewModel [AuthViewModel].
 *
 * Aquesta classe permet injectar els casos d'ús necessaris al ViewModel d'autenticació.
 *
 * @param authUseCases Casos d'ús relacionats amb l'autenticació.
 */
class AuthViewModelFactory(private val authUseCases: AuthUseCases) : ViewModelProvider.Factory {

    /**
     * Crea una nova instància de [AuthViewModel] si la classe sol·licitada coincideix.
     *
     * @param modelClass La classe del ViewModel a crear.
     * @return Una nova instància de [AuthViewModel].
     * @throws IllegalArgumentException Si la classe no és compatible amb [AuthViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authUseCases) as T
        }
        throw IllegalArgumentException("ViewModel no trobat")
    }
}

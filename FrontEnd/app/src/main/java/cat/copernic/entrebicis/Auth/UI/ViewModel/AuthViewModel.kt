package cat.copernic.entrebicis.route.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.entrebicis.Auth.Domain.AuthUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel per gestionar la lògica de recuperació de contrasenya.
 *
 * Aquesta classe actua com a pont entre la interfície d'usuari i els casos d'ús
 * d'autenticació, encapsulant la lògica de negoci i exposant l'estat mitjançant
 * [StateFlow].
 *
 * @param authUseCases Casos d'ús relacionats amb l'autenticació.
 */
class AuthViewModel(private val authUseCases: AuthUseCases) : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _error = MutableStateFlow<Boolean?>(null)
    val error: StateFlow<Boolean?> = _error

    /**
     * Neteja el missatge actual de l'estat.
     */
    fun clearMessage() {
        _message.value = ""
    }

    /**
     * Inicia el procés de recuperació de contrasenya per a l'email especificat.
     *
     * Fa una crida al cas d'ús de recuperació i actualitza l'estat [_error] i [_message]
     * segons el resultat.
     *
     * @param email Correu electrònic de l'usuari que vol recuperar la contrasenya.
     */
    fun recoverPassword(email: String) {
        viewModelScope.launch {
            try {
                val response = authUseCases.recoverPassword(email)
                if (!response.isSuccessful) {
                    _error.value = true
                    _message.value = "Error: ${response.errorBody()?.string()}"
                } else {
                    _error.value = false
                }
            } catch (e: Exception) {
                _error.value = false
                _message.value = "Error de connexió: ${e.message}"
            }
        }
    }

    /**
     * Reinicia l'estat d'error per permetre nous intents sense mostrar missatges anteriors.
     */
    fun errorReset() {
        _error.value = null
    }
}

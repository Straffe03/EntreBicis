package cat.copernic.entrebicis.Users.UI.ViewModel

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.entrebicis.Core.Model.SystemParameter
import cat.copernic.entrebicis.Core.Model.User
import cat.copernic.entrebicis.Users.Domain.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * ViewModel encarregat de gestionar les operacions i l'estat relacionats amb l'usuari.
 *
 * @param useCases Casos d'ús que encapsulen la lògica de negoci d'usuaris.
 */
class UserViewModel(private val useCases: UseCases) : ViewModel() {

    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess: StateFlow<Boolean?> = _loginSuccess

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _parametres = MutableStateFlow<SystemParameter?>(null)
    val parametres: StateFlow<SystemParameter?> = _parametres

    private val _rutaActiva = MutableStateFlow<Boolean>(false)
    val rutaActiva: StateFlow<Boolean> = _rutaActiva

    private val _errorMsg = MutableStateFlow<String?>("Error Desconegut")
    val errorMsg: StateFlow<String?> = _errorMsg

    var _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    init {
        getParameters()
        Log.d("UserViewModel", "VIEWMODEL CREAT 🧠, PARAMETRES: ${parametres.value}")
    }

    /**
     * Actualitza l'estat de la ruta activa invertint el seu valor actual.
     */
    fun actualizarRutaActiva() {
        _rutaActiva.value = !_rutaActiva.value
        Log.d("UserViewModel", "Ruta activa canviada a: ${_rutaActiva.value}")
    }

    /**
     * Inicia sessió amb les credencials proporcionades.
     *
     * @param email El correu electrònic de l'usuari.
     * @param password La contrasenya.
     */
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            Log.d("UserViewModel", "Intentant iniciar sessió amb email: $email")
            val response = useCases.loginUser(email, password)
            _loginSuccess.value = response.isSuccessful
            if (response.isSuccessful) {
                val userResponse = useCases.getUser(email)
                if (userResponse.isSuccessful) {
                    val user = userResponse.body()
                    _currentUser.value = user
                    _loginSuccess.value = true
                    Log.d("UserViewModel", "Sessió iniciada correctament.")
                } else {
                    _currentUser.value = null
                    _loginSuccess.value = false
                    Log.e("UserViewModel", "Error obtenint l'usuari després del login.")
                }
            } else {
                Log.e("UserViewModel", "Error d'inici de sessió: ${response.code()}")
            }
        }
    }

    /**
     * Tanca la sessió de l'usuari actual.
     */
    fun logoutUser() {
        _currentUser.value = null
        _loginSuccess.value = null
        Log.d("UserViewModel", "Sessió tancada correctament.")
    }

    /**
     * Converteix una cadena Base64 a un Bitmap.
     *
     * @param base64 La cadena Base64 a convertir.
     * @return El Bitmap resultant o null si hi ha un error.
     */
    fun base64ToBitmap(base64: String): ImageBitmap? {
        return try {
            val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
            val byteArrayInputStream = ByteArrayInputStream(decodedBytes)
            val bitmap = BitmapFactory.decodeStream(byteArrayInputStream)
            bitmap.asImageBitmap()
        } catch (e: Exception) {
            Log.e("UserViewModel", "Error convertint Base64 a Bitmap", e)
            null
        }
    }

    /**
     * Converteix un URI a una cadena Base64.
     *
     * @param context El context de l'aplicació.
     * @param uri El URI de la imatge.
     * @return La cadena Base64 resultant o null si hi ha un error.
     */
    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val byteArrayOutputStream = ByteArrayOutputStream()

            inputStream?.use { stream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (stream.read(buffer).also { bytesRead = it } != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead)
                }
            }

            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e("UserViewModel", "Error convertint URI a Base64", e)
            null
        }
    }

    /**
     * Actualitza les dades d'un usuari.
     *
     * @param updatedUser L'usuari amb les dades modificades.
     */
    fun updateUser(updatedUser: User) {
        viewModelScope.launch {
            Log.d("UserViewModel", "Actualitzant usuari amb email: ${updatedUser.email}")
            val response = useCases.updateUser(updatedUser)
            _updateSuccess.value = response.isSuccessful
            if (response.isSuccessful) {
                val userResponse = useCases.getUser(updatedUser.email)
                if (userResponse.isSuccessful) {
                    val user = userResponse.body()
                    _currentUser.value = user
                    Log.d("UserViewModel", "Usuari actualitzat correctament.")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error desconegut del servidor"
                _errorMsg.value = errorMessage
                Log.e("UserViewModel", "Error actualitzant usuari: $errorMessage")
            }
        }
    }

    /**
     * Obté els paràmetres del sistema de manera asíncrona.
     */
    fun getParameters() {
        viewModelScope.launch {
            val response = useCases.getParameters()
            if (response.isSuccessful) {
                val parametres = response.body()
                _parametres.value = parametres
                Log.d("UserViewModel", "Paràmetres del sistema obtinguts correctament.")
            } else {
                Log.e("UserViewModel", "Error obtenint els paràmetres del sistema.")
            }
        }
    }
}

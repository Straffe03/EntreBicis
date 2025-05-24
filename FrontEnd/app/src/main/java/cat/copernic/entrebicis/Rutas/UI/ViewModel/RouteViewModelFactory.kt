package cat.copernic.entrebicis.Rutas.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.entrebicis.Rutas.Domain.RouteUseCases
import cat.copernic.entrebicis.route.ui.viewmodel.RouteViewModel

/**
 * Factory per crear instàncies de [RouteViewModel].
 *
 * @param routeUseCases Els casos d'ús per a les operacions de rutes.
 */
class RouteViewModelFactory(private val routeUseCases: RouteUseCases) : ViewModelProvider.Factory {

    /**
     * Crea una nova instància de [RouteViewModel].
     *
     * @param modelClass La classe del ViewModel a crear.
     * @return Una nova instància de [RouteViewModel].
     * @throws IllegalArgumentException Si la classe no és assignable a [RouteViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RouteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RouteViewModel(routeUseCases) as T
        }
        throw IllegalArgumentException("Viewmodel not found")
    }
}

package cat.copernic.entrebicis.Rutas.Domain

import cat.copernic.entrebicis.Core.Model.GPSPoint
import cat.copernic.entrebicis.Core.Model.Route
import cat.copernic.entrebicis.Core.Model.User
import cat.copernic.entrebicis.Rutas.Data.RouteRepository
import android.util.Log

/**
 * Classe que encapsula els casos d'ús per a les operacions de rutes.
 *
 * @param repository El repositori de rutes utilitzat per accedir a les dades.
 */
class RouteUseCases(private val repository: RouteRepository) {

    companion object {
        private const val TAG = "RouteUseCases"
    }

    /**
     * Crea una nova ruta.
     *
     * @param route Ruta a crear.
     * @return Resposta HTTP amb la ruta creada.
     */
    suspend fun createRoute(route: Route) = repository.createRoute(route).also {
        Log.d(TAG, "S'ha sol·licitat la creació d'una nova ruta.")
    }

    /**
     * Actualitza una ruta existent.
     *
     * @param route Ruta amb les dades modificades.
     * @return Resposta HTTP amb la ruta actualitzada.
     */
    suspend fun updateRoute(route: Route) = repository.updateRoute(route).also {
        Log.d(TAG, "S'ha sol·licitat l'actualització de la ruta amb ID: ${route.id}")
    }

    /**
     * Obté una ruta concreta pel seu identificador.
     *
     * @param routeId Identificador de la ruta.
     * @return Resposta HTTP amb la ruta corresponent.
     */
    suspend fun getRoute(routeId: Long) = repository.getRoute(routeId).also {
        Log.d(TAG, "S'ha sol·licitat la ruta amb ID: $routeId")
    }

    /**
     * Desa una llista de punts GPS.
     *
     * @param puntos Llista de punts GPS a desar.
     * @return Resposta HTTP amb l'estat de l'operació.
     */
    suspend fun guardarPuntosGps(puntos: List<GPSPoint>) = repository.guardarPuntosGps(puntos).also {
        Log.d(TAG, "S'ha sol·licitat desar ${puntos.size} punts GPS.")
    }

    /**
     * Obté totes les rutes associades a un usuari.
     *
     * @param user Correu electrònic o identificador de l'usuari.
     * @return Resposta HTTP amb la llista de rutes.
     */
    suspend fun getRoutesByUser(user: String) = repository.getRoutesByUser(user).also {
        Log.d(TAG, "S'han sol·licitat les rutes de l'usuari: $user")
    }
}

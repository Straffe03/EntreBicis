package cat.copernic.entrebicis.Rutas.Data

import MarioOlaya.Projecte4.EntreBicis.entity.Reward
import cat.copernic.entrebicis.Core.Model.GPSPoint
import cat.copernic.entrebicis.Core.Model.Route
import cat.copernic.entrebicis.Core.Model.User
import cat.copernic.entrebicis.Core.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Response
import android.util.Log

/**
 * Repositori per gestionar les operacions relacionades amb rutes mitjançant la API REST.
 */
class RouteRepository {

    companion object {
        private const val TAG = "RouteRepository"
    }

    /**
     * Crea una nova ruta.
     *
     * @param route Ruta a crear.
     * @return Resposta HTTP amb la ruta creada.
     */
    suspend fun createRoute(route: Route): Response<Route> {
        Log.d(TAG, "S'està creant una nova ruta.")
        return RetrofitInstance.routeApi.createRoute(route)
    }

    /**
     * Actualitza una ruta existent.
     *
     * @param route Ruta amb les dades actualitzades.
     * @return Resposta HTTP amb la ruta actualitzada.
     */
    suspend fun updateRoute(route: Route): Response<Route> {
        Log.d(TAG, "S'està actualitzant la ruta amb ID: ${route.id}")
        return RetrofitInstance.routeApi.updateRoute(route)
    }

    /**
     * Obté una ruta concreta pel seu identificador.
     *
     * @param routeId Identificador únic de la ruta.
     * @return Resposta HTTP amb la ruta corresponent.
     */
    suspend fun getRoute(routeId: Long): Response<Route> {
        Log.d(TAG, "S'està obtenint la ruta amb ID: $routeId")
        return RetrofitInstance.routeApi.getRoute(routeId)
    }

    /**
     * Desa una llista de punts GPS associats a una ruta.
     *
     * @param puntos Llista de punts GPS a desar.
     * @return Resposta HTTP amb l'estat de l'operació.
     */
    suspend fun guardarPuntosGps(puntos: List<GPSPoint>): Response<ResponseBody> {
        Log.d(TAG, "S'estan desant ${puntos.size} punts GPS.")
        return RetrofitInstance.routeApi.guardarPuntosGps(puntos)
    }

    /**
     * Obté totes les rutes associades a un usuari.
     *
     * @param user Correu electrònic o identificador de l'usuari.
     * @return Resposta HTTP amb la llista de rutes de l'usuari.
     */
    suspend fun getRoutesByUser(user: String): Response<List<Route>> {
        Log.d(TAG, "S'estan obtenint les rutes de l'usuari: $user")
        return RetrofitInstance.routeApi.getRoutesByUser(user)
    }
}

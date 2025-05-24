package cat.copernic.entrebicis.Rutas.Data

import cat.copernic.entrebicis.Core.Model.GPSPoint
import cat.copernic.entrebicis.Core.Model.Route
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interfície per a les operacions de la API REST de rutes.
 * Defineix els mètodes per crear, actualitzar i recuperar rutes i punts GPS.
 */
interface RouteApiRest {

    /**
     * Crea una nova ruta.
     *
     * @param route Ruta a crear.
     * @return Resposta HTTP amb la ruta creada.
     */
    @POST("route/create")
    suspend fun createRoute(@Body route: Route): Response<Route>

    /**
     * Actualitza una ruta existent.
     *
     * @param ruta Ruta amb les dades actualitzades.
     * @return Resposta HTTP amb la ruta actualitzada.
     */
    @PUT("route/update/{id}")
    suspend fun updateRoute(@Body ruta: Route): Response<Route>

    /**
     * Obté una ruta concreta pel seu identificador.
     *
     * @param routeId Identificador de la ruta.
     * @return Resposta HTTP amb la ruta sol·licitada.
     */
    @GET("route/view/{routeId}")
    suspend fun getRoute(@Path("routeId") routeId: Long): Response<Route>

    /**
     * Desa una llista de punts GPS associats a una ruta.
     *
     * @param puntos Llista de punts GPS a desar.
     * @return Resposta HTTP indicant l'estat de l'operació.
     */
    @POST("route/gpspoints")
    suspend fun guardarPuntosGps(@Body puntos: List<GPSPoint>): Response<ResponseBody>

    /**
     * Obté totes les rutes d'un usuari.
     *
     * @param userId Identificador de l'usuari.
     * @return Resposta HTTP amb la llista de rutes de l'usuari.
     */
    @GET("route/list/{userId}")
    suspend fun getRoutesByUser(@Path("userId") userId: String): Response<List<Route>>
}

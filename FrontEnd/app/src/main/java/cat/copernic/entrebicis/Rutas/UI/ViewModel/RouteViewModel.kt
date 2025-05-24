package cat.copernic.entrebicis.route.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.entrebicis.Core.Model.GPSPoint
import cat.copernic.entrebicis.Core.Model.Route
import cat.copernic.entrebicis.Core.Model.RouteState
import cat.copernic.entrebicis.Core.Model.SystemParameter
import cat.copernic.entrebicis.Core.Model.User
import cat.copernic.entrebicis.Rutas.Domain.RouteUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * ViewModel que gestiona l'estat i operacions relacionades amb les rutes.
 *
 * @param routeUseCases Casos d'ús relacionats amb les rutes.
 */
class RouteViewModel(private val routeUseCases: RouteUseCases) : ViewModel() {

    private var _currentRoute = MutableStateFlow<Route?>(null)
    val currentRoute: StateFlow<Route?> = _currentRoute

    var _startingTime = MutableStateFlow<Long>(0)
    val startingTime: StateFlow<Long> = _startingTime

    var _time = MutableStateFlow<Long>(0)
    val time: StateFlow<Long> = _time

    var _distance = MutableStateFlow<Double>(0.0)
    val distance: StateFlow<Double> = _distance

    var _maxVel = MutableStateFlow<Double>(0.0)
    val maxVel: StateFlow<Double> = _maxVel

    private val _gpsPoints = mutableListOf<GPSPoint>()
    val gpsPoints: List<GPSPoint> get() = _gpsPoints

    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> = _routes

    private val _route = MutableStateFlow<Route?>(null)
    val route: StateFlow<Route?> = _route

    /**
     * Carrega totes les rutes disponibles des del backend mitjançant els casos d'ús.
     *
     * @param user L'usuari del qual es volen obtenir les rutes.
     */
    fun listRoutes(user: User) {
        viewModelScope.launch {
            try {
                val response = routeUseCases.getRoutesByUser(user.email)
                if (response.isSuccessful) {
                    _routes.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Afegeix un punt GPS a la llista de punts de la ruta actual.
     *
     * @param point Punt GPS a afegir.
     */
    fun addGpsPoint(point: GPSPoint) {
        _gpsPoints.add(point)
    }

    /**
     * Neteja la llista de punts GPS acumulats.
     */
    fun restartGpsPoints() {
        _gpsPoints.clear()
    }

    /**
     * Inicia una nova ruta per a l'usuari especificat.
     *
     * @param user L'usuari que comença la ruta.
     */
    fun iniciarRuta(user: User) {
        viewModelScope.launch {
            val route = Route(null, RouteState.PENDENT, false, null, 0.0, 0.0, 0.0, 0.0, 0.0, null, user)
            val response = routeUseCases.createRoute(route)
            if (response.isSuccessful) {
                _currentRoute.value = response.body()
            }
            _startingTime.value = currentTimeMillis()
        }
    }

    /**
     * Finalitza la ruta actual, actualitza les dades i les desa al backend.
     *
     * @param user L'usuari que ha completat la ruta.
     * @param parameters Paràmetres del sistema utilitzats per calcular els punts.
     */
    fun finalizarRuta(user: User, parameters: SystemParameter) {
        viewModelScope.launch {
            Log.d("RouteViewModel", "Guardando puntos gps: ${gpsPoints.size}")
            val route : Route?
            route = currentRoute.value
            for (gpsPoint in _gpsPoints) {
                gpsPoint.route = route
            }
            guardarPuntosGps(gpsPoints)
            _time.value = (currentTimeMillis() - startingTime.value) / 1000

            val minut = time.value / 60
            val secRest = time.value % 60

            _currentRoute.value!!.time = redondejar(minut + (secRest / 100.0))
            _currentRoute.value!!.distance = redondejar(distance.value)
            Log.d("RouteViewModel", "Finalitzant ruta. TEMPS: ${time.value}")
            val totalSeconds = minut * 60 + secRest
            if (totalSeconds > 0) {
                _currentRoute.value!!.velocity = redondejar((distance.value * 3.6) / totalSeconds)
            } else {
                _currentRoute.value!!.velocity = 0.0
            }
                _currentRoute.value!!.maxVelocity = redondejar(maxVel.value * 3.6)

            _currentRoute.value!!.points = redondejar((distance.value / 1000) * parameters.pointsPerKm)

            _currentRoute.value!!.state = RouteState.FINALITZADA
            _currentRoute.value!!.user = user

            val ruta: Route = _currentRoute.value!!

            val response = routeUseCases.updateRoute(ruta)
            if (response.isSuccessful) {
                _currentRoute.value = response.body()
            }


            restartMaxVel()
            restartDistance()
            restartTime()
            restartGpsPoints()
        }
    }

    /**
     * Arrodoneix un valor decimal a dues xifres.
     *
     * @param valor Valor a arrodonir.
     * @return Valor arrodonit amb dues xifres decimals.
     */
    fun redondejar(valor: Double): Double {
        return BigDecimal(valor).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    /**
     * Reinicia el temps acumulat de la ruta.
     */
    fun restartTime() {
        _time.value = 0
    }

    /**
     * Afegeix distància a la ruta actual.
     *
     * @param distance Distància a afegir (en metres).
     */
    fun addDistance(distance: Float) {
        _distance.value += distance.toDouble()
    }

    /**
     * Reinicia la distància acumulada.
     */
    fun restartDistance() {
        _distance.value = 0.0
    }

    /**
     * Estableix una nova velocitat màxima temporal.
     *
     * @param vel Velocitat en m/s.
     */
    fun modifyVel(vel: Float) {
        _maxVel.value = vel.toDouble()
    }

    /**
     * Reinicia la velocitat màxima enregistrada.
     */
    fun restartMaxVel() {
        _maxVel.value = 0.0
    }

    /**
     * Obté una ruta concreta a partir del seu identificador.
     *
     * @param routeId Identificador de la ruta.
     */
    fun getRoute(routeId: Long) {
        viewModelScope.launch {
            val response = routeUseCases.getRoute(routeId)
            if (response.isSuccessful) {
                _route.value = response.body()
            }
        }
    }

    /**
     * Desa una llista de punts GPS al servidor.
     *
     * @param puntos Llista de punts GPS a desar.
     */
    fun guardarPuntosGps(puntos: List<GPSPoint>) {
        viewModelScope.launch {
            try {
                Log.d("RouteViewModel", "Desant punts GPS: ${puntos.size}")
                val response = routeUseCases.guardarPuntosGps(puntos)
                if (response.isSuccessful) {
                    Log.d("RouteViewModel", "Punts GPS desats correctament")
                } else {
                    Log.e("RouteViewModel", "Error en desar punts GPS: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("RouteViewModel", "Excepció guardant punts GPS", e)
            }
        }
    }
}

package cat.copernic.entrebicis.Rutas.UI.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavController
import cat.copernic.entrebicis.Core.ui.components.BottomNavBar
import cat.copernic.entrebicis.Core.ui.components.TopBar
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import cat.copernic.entrebicis.Core.Model.GPSPoint
import cat.copernic.entrebicis.Core.Model.Route
import cat.copernic.entrebicis.Rutas.Data.RouteRepository
import cat.copernic.entrebicis.Rutas.Domain.RouteUseCases
import cat.copernic.entrebicis.Rutas.UI.ViewModel.RouteViewModelFactory
import cat.copernic.entrebicis.Users.Data.UserRepository
import cat.copernic.entrebicis.Users.Domain.UseCases
import cat.copernic.entrebicis.route.ui.viewmodel.RouteViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import java.sql.Timestamp
import java.time.LocalDateTime

@Composable
fun MenuScreen(navController: NavController, userViewModel: UserViewModel) {
    val rutaActiva by userViewModel.rutaActiva.collectAsState()

    val routeUseCases = RouteUseCases(RouteRepository())

    val routeViewModel: RouteViewModel =
        viewModel(factory = RouteViewModelFactory(routeUseCases))

    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                if (rutaActiva) {
                    routeViewModel.finalizarRuta(userViewModel.currentUser.value!!, userViewModel.parametres.value!!)
                    userViewModel.actualizarRutaActiva()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasLocationPermission = isGranted
        }

    LaunchedEffect(Unit) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            hasLocationPermission = true
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB0C4DE))// Blau suau
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Header
        TopBar(navController, userViewModel)

        // Espai per al mapa
        MapView(rutaActiva, routeViewModel, userViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Botons de Ruta
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!rutaActiva) {
                RouteButton(
                    text = "Iniciar Ruta",
                    color = Color(0xFF4A90E2), userViewModel, rutaActiva, routeViewModel
                ) // Blau
            } else {
                RouteButton(
                    text = "Finalizar Ruta",
                    color = Color(0xFFE94E4E),
                    userViewModel, rutaActiva, routeViewModel
                ) // Vermell
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation Bar
        BottomNavBar(navController, userViewModel)
    }
}

@Composable
fun RouteButton(
    text: String,
    color: Color,
    userViewModel: UserViewModel,
    rutaActiva: Boolean,
    routeViewModel: RouteViewModel
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val parameters by userViewModel.parametres.collectAsState()
    Button(
        onClick = {
            if (rutaActiva) {
                routeViewModel.finalizarRuta(currentUser!!, parameters!!)
            } else {
                routeViewModel.iniciarRuta(currentUser!!)
            }
            userViewModel.actualizarRutaActiva()
        },
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(50.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(text = text, fontSize = 18.sp, color = Color.White)
    }
}

@SuppressLint("MissingPermission")
@Composable
fun MapView(rutaActiva: Boolean, routeViewModel: RouteViewModel, userViewModel: UserViewModel) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val context = LocalContext.current
    val parameters by userViewModel.parametres.collectAsState()
    val currentRoute by routeViewModel.currentRoute.collectAsState()
    val maxVel by routeViewModel.maxVel.collectAsState()

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val routePoints = remember { mutableStateListOf<LatLng>() }
    var previousLocation by remember { mutableStateOf<LatLng?>(null) }
    var isStationary by remember { mutableStateOf(false) }
    val movementThreshold = 1f


    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.create().apply {
        interval = 1000           // Cada 1 segundo.
        fastestInterval = 500     // Intervalo más rápido de 500 ms.
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    var previousLocationTime by remember { mutableStateOf<Long?>(null) }
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val newLocation = LatLng(location.latitude, location.longitude)
                    currentLocation = newLocation
                    if (previousLocation == null) {
                        previousLocation = newLocation
                        previousLocationTime = location.time
                        routePoints.add(newLocation)
                        val now = System.currentTimeMillis()
                        routeViewModel.addGpsPoint(
                            GPSPoint(
                                id = null, // Se genera en la base de datos
                                latitude = newLocation.latitude,
                                longitude = newLocation.longitude,
                                timestamp = Timestamp(now).toString(),
                                route = null
                            )
                        )
                        isStationary = false
                    } else {
                        val distance = distanceBetween(previousLocation!!, newLocation)
                        val timeDeltaSeconds = (location.time - (previousLocationTime ?: location.time)) / 1000.0

                        if (timeDeltaSeconds > 0) {
                            val velocidadInstantanea = distance / timeDeltaSeconds
                            if (velocidadInstantanea > routeViewModel.maxVel.value) {
                                routeViewModel.modifyVel(velocidadInstantanea.toFloat())
                            }
                        }
                        routeViewModel.addDistance(distance)
                        if (distance < movementThreshold) {
                            isStationary = true
                        } else {
                            isStationary = false
                            previousLocation = newLocation
                            routePoints.add(newLocation)
                            previousLocationTime = location.time
                            val now = System.currentTimeMillis()
                            routeViewModel.addGpsPoint(
                                GPSPoint(
                                    id = null, // Se genera en la base de datos
                                    latitude = newLocation.latitude,
                                    longitude = newLocation.longitude,
                                    timestamp = Timestamp(now).toString(),
                                    route = null
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    var isListeningLocation by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        if (!isListeningLocation) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            isListeningLocation = true
        }

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            isListeningLocation = false
        }
    }

    LaunchedEffect(isStationary) {
        if (isStationary) {
            delay(parameters!!.maxStopTime.toLong()*1000*60)
            if (isStationary) {
                if (rutaActiva) {
                    routeViewModel.finalizarRuta(currentUser!!, parameters!!)
                    userViewModel.actualizarRutaActiva()
                }
                routePoints.clear()
                routeViewModel.restartMaxVel()
                routeViewModel.restartGpsPoints()
                routeViewModel.restartDistance()
                previousLocation = currentLocation

                currentLocation?.let { routePoints.add(it) }
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = currentLocation?.let {
            CameraPosition.fromLatLngZoom(it, 19f)
        } ?: CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 1f)
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(it, 19f)
            )
        }
    }

    LaunchedEffect(rutaActiva) {
        if (rutaActiva) {
            routePoints.clear()
            routeViewModel.restartMaxVel()
            routeViewModel.restartGpsPoints()
            routeViewModel.restartDistance()
            previousLocation = null
            currentLocation = null
        }
    }

    // Muestra solo el mapa ocupando toda la pantalla.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(16.dp)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(myLocationButtonEnabled = false)
        ) {
            if (rutaActiva) {
                Polyline(
                    points = routePoints.toList(),
                    color = androidx.compose.ui.graphics.Color.Red,
                    width = 8f
                )
            }
        }
    }
}

fun distanceBetween(a: LatLng, b: LatLng): Float {
    val results = FloatArray(1)
    Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, results)
    return results[0]
}

@Preview
@Composable
fun MenuScreenPreview() {
    val fakeNavController =
        rememberNavController() // ✅ Crear un NavController fals per la preview
    MenuScreen(
        navController = fakeNavController,
        userViewModel = UserViewModel(UseCases(UserRepository()))
    )  // ✅ Passar-lo a LoginScreen
}
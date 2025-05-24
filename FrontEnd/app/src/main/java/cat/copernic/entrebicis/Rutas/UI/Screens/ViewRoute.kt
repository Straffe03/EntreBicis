package cat.copernic.entrebicis.Rutas.UI.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.entrebicis.Core.ui.components.BottomNavBar
import cat.copernic.entrebicis.Core.ui.components.TopBar
import cat.copernic.entrebicis.Rutas.Data.RouteRepository
import cat.copernic.entrebicis.Rutas.Domain.RouteUseCases
import cat.copernic.entrebicis.Rutas.UI.ViewModel.RouteViewModelFactory
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel
import cat.copernic.entrebicis.route.ui.viewmodel.RouteViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline


@Composable
fun ViewRoute(
    navController: NavController,
    userViewModel: UserViewModel,
) {
    val routeUseCases = RouteUseCases(RouteRepository())
    val routeViewModel: RouteViewModel = viewModel(factory = RouteViewModelFactory(routeUseCases))

    val routeId = remember {
        navController.currentBackStackEntry?.arguments?.getString("routeId")
    } ?: return // Si no hay ID, salir de la función

    val route by routeViewModel.route.collectAsState()

    LaunchedEffect(routeId) {
        routeViewModel.getRoute(routeId.toLong())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9FA8DA))
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(navController, userViewModel)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Visualitzar Ruta",
                fontSize = 36.sp,
                color = Color.Black,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFB3E5FC), shape = RoundedCornerShape(12.dp))
                    .padding(8.dp)
                    .fillMaxWidth(0.9f)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    route?.let {
                        Text(text = "Data: ${it.date}", fontSize = 28.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(10.dp))

                        val points = it.gpsPoints?.map { p -> LatLng(p.latitude, p.longitude) }

                        GoogleMap(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            cameraPositionState = rememberCameraPositionState {
                                if (points != null) {
                                    position = CameraPosition.fromLatLngZoom(
                                        points.firstOrNull() ?: LatLng(0.0, 0.0),
                                        15f
                                    )
                                }
                            }
                        ) {
                            if (points != null) {
                                Polyline(points = points)
                            }
                            points?.forEach { p ->
                                Marker(
                                    state = MarkerState(position = p),
                                    title = "Punt GPS",
                                    snippet = "Lat: ${p.latitude}, Lng: ${p.longitude}"
                                )
                            }

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Distància: ${it.distance} m", fontSize = 20.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Temps: ${it.time} min", fontSize = 20.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Velocitat mitjana: ${it.velocity} km/h", fontSize = 20.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Velocitat màxima: ${it.maxVelocity} km/h", fontSize = 20.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Punts: ${it.points}", fontSize = 20.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Estat: ${if (it.validation) "Validada" else "No validada"}",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        BottomNavBar(navController, userViewModel, 1)
    }
}

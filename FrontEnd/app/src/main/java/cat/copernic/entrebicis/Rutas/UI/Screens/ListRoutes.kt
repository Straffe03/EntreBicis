package cat.copernic.entrebicis.Rutas.UI.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.entrebicis.Core.Model.RouteState
import cat.copernic.entrebicis.Core.ui.components.BottomNavBar
import cat.copernic.entrebicis.Core.ui.components.TopBar
import cat.copernic.entrebicis.Rutas.Data.RouteRepository
import cat.copernic.entrebicis.Rutas.Domain.RouteUseCases
import cat.copernic.entrebicis.Rutas.UI.ViewModel.RouteViewModelFactory
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel
import cat.copernic.entrebicis.route.ui.viewmodel.RouteViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import cat.copernic.entrebicis.Core.Model.Route
import cat.copernic.entrebicis.Reward.UI.Screens.ListRewardScreen
import cat.copernic.entrebicis.Users.Data.UserRepository
import cat.copernic.entrebicis.Users.Domain.UseCases


@Composable
fun ListRoutes(navController: NavController, userViewModel: UserViewModel) {
    val routeUseCases = RouteUseCases(RouteRepository())
    val routeViewModel: RouteViewModel = viewModel(factory = RouteViewModelFactory(routeUseCases))
    val user by userViewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        routeViewModel.listRoutes(user!!)
    }

    val routes by routeViewModel.routes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF99CCFF))
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(navController, userViewModel)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Llistat de Rutes",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            routes.forEach { route ->
                RouteCard(route, navController)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        BottomNavBar(navController, userViewModel, 1)
    }
}

@Composable
fun RouteCard(route: Route, navController: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC0D9FF)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { navController.navigate("viewRoute/${route.id}") }

    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "Data: ${route.date}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Distància: ${route.distance} metres", fontSize = 18.sp)
            Text(text = "Temps: ${route.time} minuts", fontSize = 18.sp)
            Text(text = "Velocitat mitjana: ${route.velocity} km/h", fontSize = 18.sp)
            Text(text = "Velocitat màxima: ${route.maxVelocity} km/h", fontSize = 18.sp)
            Text(text = "Saldo atorgat: ${route.points} punts", fontSize = 18.sp)
            Text(
                text = "Estat: ${if (route.validation) "Validada" else "No validada"}",
                fontSize = 18.sp
            )
        }
    }
}

@Preview
@Composable
fun ListRoutesPreview() {
    val fakeNavController =
        rememberNavController() // ✅ Crear un NavController fals per la preview
    ListRoutes(
        navController = fakeNavController,
        userViewModel = UserViewModel(UseCases(UserRepository()))
    )  // ✅ Passar-lo a LoginScreen
}


package cat.copernic.entrebicis.Users.UI.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.entrebicis.Core.Model.User
import cat.copernic.entrebicis.Core.ui.components.BottomNavBar
import cat.copernic.entrebicis.Core.ui.components.TopBar
import cat.copernic.entrebicis.R
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel

@Composable
fun ProfileScreen(navController: NavController, userViewModel: UserViewModel) {
    val currentUser by userViewModel.currentUser.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9FA8DA))
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        TopBar(navController, userViewModel)

        Spacer(modifier = Modifier.height(8.dp))

        // Profile Section
        ProfileSection(currentUser, userViewModel)


        // Historial de Rutes
        Button(
            onClick = {
                navController.navigate("listRoutes")
            },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6)),
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(0.8f)
        ) {
            Text("Historial de Rutes", color = Color.DarkGray, fontSize = 22.sp)
        }

        // Historial de Recompenses
        Button(
            onClick = {
                navController.navigate("ownRewards/${currentUser?.email}")
            },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6)),
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(0.8f)
        ) {
            Text("Historial de Recompenses", color = Color.DarkGray, fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))
        // Modify Button
        Button(
            onClick = { navController.navigate("modifyUser") },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5)),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Modificar", color = Color.DarkGray, fontSize = 22.sp)
        }


        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation
        BottomNavBar(navController, userViewModel, 5)
    }


}


@Composable
fun ProfileSection(currentUser: User?, userViewModel: UserViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xFFB3E5FC), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {

        if (currentUser != null) {
            Text(
                text = currentUser.name,
                fontSize = 50.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = currentUser.surname,
                fontSize = 40.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }

        Spacer(modifier = Modifier.height(30.dp))

        if (currentUser?.image == null) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Sense foto de perfil",
                Modifier.size(220.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            val imageBitmap = currentUser.image.let {
                userViewModel.base64ToBitmap(it)
            }
            if (imageBitmap != null) {
                Image(
                    imageBitmap,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        UserInfoSection(currentUser)
    }
}

@Composable
fun UserInfoSection(currentUser: User?) {
    val punts = currentUser?.points
    val email = currentUser?.email
    val telefon = currentUser?.phone
    Column {
        Text("Punts: $punts", fontSize = 32.sp, color = Color.Black)
        Spacer(modifier = Modifier.size(10.dp))
        Text("Email: $email", fontSize = 32.sp, color = Color.Black)
        Spacer(modifier = Modifier.size(10.dp))

        Text("Telèfon: $telefon", fontSize = 32.sp, color = Color.Black)
    }
}

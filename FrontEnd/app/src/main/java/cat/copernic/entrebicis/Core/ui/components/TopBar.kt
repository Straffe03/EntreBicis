package cat.copernic.entrebicis.Core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(navController: NavController, userViewModel: UserViewModel) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val punts = currentUser?.points ?: 0.0
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6246EA)) // Color lila fosc
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar de perfil

        /*Image(
            painter = painterResource(id = R.drawable.profile_avatar),
            contentDescription = "Perfil",
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, shape = CircleShape)
        )*/
        if (currentUser?.image == null) {
            Icon(Icons.Default.Person,
                contentDescription = "perfil",
                Modifier.size(40.dp)
                    .clickable { navController.navigate("profile") })
        } else {
            val imageBitmap = currentUser?.image?.let {
                userViewModel.base64ToBitmap(it)
            }
            if (imageBitmap != null) {
                Image(
                    imageBitmap,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White, shape = CircleShape)
                        .clickable { navController.navigate("profile") }
                )
            }
        }
        // Títol
        Text(
            text = "ENTREBICIS",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            text = "Punts: $punts",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

}


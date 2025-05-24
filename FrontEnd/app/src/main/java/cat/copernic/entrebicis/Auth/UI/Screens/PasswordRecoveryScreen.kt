package cat.copernic.entrebicis.Auth.UI.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.entrebicis.Auth.Domain.AuthUseCases
import cat.copernic.entrebicis.R
import cat.copernic.entrebicis.Rutas.Data.AuthRepository
import cat.copernic.entrebicis.Rutas.UI.ViewModel.AuthViewModelFactory
import cat.copernic.entrebicis.route.ui.viewmodel.AuthViewModel

@Composable
fun PasswordRecoveryScreen(navController: NavController) {

    val authUseCases = AuthUseCases(AuthRepository())
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authUseCases))

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    val message by viewModel.message.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(message) {
        var newMessage: String = message
        if (message.isNotEmpty()) {
            if (error != true) {
                newMessage = "S'ha enviat un correu de recuperacio"
            }
                Toast.makeText(
                    context,
                    newMessage,
                    Toast.LENGTH_LONG
                ).show()

            viewModel.clearMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFAEC6FF)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo EntreBicis",
                modifier = Modifier
                    .size(180.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "EntreBicis",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(20.dp))
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Recuperar Contrasenya",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correu electrònic") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 18.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.recoverPassword(email)
                            viewModel.errorReset()
                            navController.navigate("login")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5))
                    ) {
                        Text("Enviar correu", fontSize = 18.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (message.isNotEmpty()) {
                        Text(
                            text = message,
                            color = if ("error" in message.lowercase()) Color.Red else Color.Black,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Tornar a l'inici de sessió",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.clickable {
                            navController.navigate("login")
                        }
                    )
                }
            }
        }
    }
}


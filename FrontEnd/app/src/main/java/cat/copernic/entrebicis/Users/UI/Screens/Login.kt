package cat.copernic.entrebicis.Users.UI.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.entrebicis.R
import cat.copernic.entrebicis.Users.Data.UserRepository
import cat.copernic.entrebicis.Users.Domain.UseCases
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel

@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginSuccess by userViewModel.loginSuccess.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            navController.navigate("menu")  // Si el login és correcte, naveguem
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFAEC6FF)), // Color blau de fons
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Imatge superior (logo)
            Image(
                painter = painterResource(id = R.drawable.logo), // Afegir el logo a res/drawable
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
                        text = "Login",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Camp d'entrada per Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 18.sp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Camp d'entrada per Contrasenya
                    InputField(
                        label = "contrasenya",
                        value = password,
                        onValueChange = { password = it },
                        isPassword = true // ✅ Activar la ocultación de contraseña
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botó de confirmació
                    Button(
                        onClick = { userViewModel.loginUser(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Confirmar", fontSize = 18.sp, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    if (loginSuccess == false) {
                        Text(
                            text = "Error en el Inici de sessio, si us plau comprova el email i la contrasenya.",
                            color = Color.Red,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Text de "Has oblidat la teva contrasenya?"
                    Text(
                        text = "Has oblidat la teva contrasenya?",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.clickable {
                            // Acció a executar, com navegar a la pantalla de recuperació
                            navController.navigate("recoverPassword")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            label = { Text(label) },
            value = value,
            onValueChange = onValueChange,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    val fakeNavController =
        rememberNavController() // ✅ Crear un NavController fals per la preview
    LoginScreen(
        navController = fakeNavController,
        userViewModel = UserViewModel(UseCases(UserRepository()))
    )  // ✅ Passar-lo a LoginScreen
}
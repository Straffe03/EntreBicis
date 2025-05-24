package cat.copernic.entrebicis.Users.UI.Screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.entrebicis.Core.Model.User
import cat.copernic.entrebicis.Core.Model.UserType
import cat.copernic.entrebicis.Core.ui.components.BottomNavBar
import cat.copernic.entrebicis.Core.ui.components.TopBar
import cat.copernic.entrebicis.R
import cat.copernic.entrebicis.Users.Data.UserRepository
import cat.copernic.entrebicis.Users.Domain.UseCases
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

@Composable
fun ModifyUserScreen(navController: NavController, userViewModel: UserViewModel) {
    val user by userViewModel.currentUser.collectAsState()
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var profilePicture by remember { mutableStateOf("") }

    var oldProfilePicture by remember { mutableStateOf("") }
    if (user!!.image!= null){
        oldProfilePicture = user!!.image.toString()
    }


    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var userType by remember { mutableStateOf<UserType>(user!!.userType) }

    val context = LocalContext.current

    val updateSuccess by userViewModel.updateSuccess.collectAsState()

    val errorMsg by userViewModel.errorMsg.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            userViewModel._updateSuccess.value = null
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBFD4FF)) // Blau suau
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TopBar(navController, userViewModel)


        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputFieldEdit(
                    label = "nom", value = name
                ) {
                    name = it
                }
                InputFieldEdit(
                    label = "cognom",
                    value = surname
                ) { surname = it }

                InputFieldEdit(
                    label = "contrasenya",
                    value = password,
                    isPassword = true
                ) { password = it }

                InputFieldEdit(
                    label = "telefon",
                    value = telephone,
                    keyboardType = KeyboardType.Number
                ) { telephone = it }

                InputFieldEdit(
                    label = "Poblacio",
                    value = city
                ) { city = it }

                Text(
                    text = "Foto de perfil",
                    color = Color.Black
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        if (selectedImageUri == null && oldProfilePicture.isNotEmpty()) {
                            val imageBitmap = userViewModel.base64ToBitmap(oldProfilePicture)
                            imageBitmap?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = "Foto de perfil",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                )
                            }
                        } else if (selectedImageUri != null) {
                            profilePicture =
                                userViewModel.uriToBase64(context, selectedImageUri!!)
                                    .toString()
                            oldProfilePicture = profilePicture
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Icon(Icons.Default.Person,
                                contentDescription = "Sense foto de perfil",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Afegir imatge",
                            modifier = Modifier
                                .clickable {
                                    imagePickerLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                                .padding(top = 40.dp)
                                .clip(RoundedCornerShape(50))
                                .size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    onClick = {
                        val imageToSave = if (oldProfilePicture.isNotBlank()) oldProfilePicture else null
                        val updatedUser = User(
                            email = user!!.email,
                            password = password,
                            name = name,
                            surname = surname,
                            phone = telephone,
                            city = city,
                            signupDate = user!!.signupDate,
                            userType = userType,
                            points = user!!.points,
                            image = imageToSave,
                            observations = user!!.observations
                        )
                        Log.d("UserViewModel", "Updating user: $updatedUser")
                        userViewModel.updateUser(updatedUser)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF69B4)),
                ) {
                    Text(
                        text = "Confirmar",
                        color = Color.White,
                    )
                }
            }
        }
        LaunchedEffect(updateSuccess) {
            updateSuccess?.let { success ->
                if (success) {
                    Toast.makeText(context, "Perfil actualitzat correctament", Toast.LENGTH_LONG)
                        .show()
                    navController.navigate("profile")
                } else {
                    Toast.makeText(
                        context,
                        errorMsg, Toast.LENGTH_LONG
                    ).show()
                }
                userViewModel._updateSuccess.value = null
            }

        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(navController, userViewModel, 5)

    }

}

@Composable
fun InputFieldEdit(
    label: String,
    value: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = label, color = Color.Black, fontSize = 14.sp)
        TextField(
            value = value,
            placeholder = { Text(text = "Deixa-ho en blanc si no vols modificar aquest camp") },
            onValueChange = onValueChange,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),

            )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Preview
@Composable
fun ModifyScreenPreview() {
    val fakeNavController =
        rememberNavController() // ✅ Crear un NavController fals per la preview
    ModifyUserScreen(
        navController = fakeNavController,
        userViewModel = UserViewModel(UseCases(UserRepository()))
    )  // ✅ Passar-lo a LoginScreen
}
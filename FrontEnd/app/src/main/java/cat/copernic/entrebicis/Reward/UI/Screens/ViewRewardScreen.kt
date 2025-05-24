package cat.copernic.entrebicis.Reward.UI.Screens

import MarioOlaya.Projecte4.EntreBicis.entity.Reward
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.entrebicis.Core.Model.StateType
import cat.copernic.entrebicis.Core.ui.components.BottomNavBar
import cat.copernic.entrebicis.Core.ui.components.TopBar
import cat.copernic.entrebicis.R
import cat.copernic.entrebicis.Reward.Data.RewardRepository
import cat.copernic.entrebicis.Reward.Domain.RewardUseCases
import cat.copernic.entrebicis.Reward.UI.ViewModel.RewardViewModel
import cat.copernic.entrebicis.Reward.UI.ViewModel.RewardViewModelFactory
import cat.copernic.entrebicis.Users.Data.UserRepository
import cat.copernic.entrebicis.Users.Domain.UseCases
import cat.copernic.entrebicis.Users.UI.Screens.LoginScreen
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel

@Composable
fun ViewRewardScreen(navController: NavController, userViewModel: UserViewModel) {
    val currentUser by userViewModel.currentUser.collectAsState()

    val rewardUseCases = RewardUseCases(RewardRepository())
    val rewardViewModel: RewardViewModel =
        viewModel(factory = RewardViewModelFactory(rewardUseCases))

    val id = remember {
        navController.currentBackStackEntry?.arguments?.getString("rewardId")
    } ?: return // Si no hay ID, salir de la función

    LaunchedEffect(Unit) {
        rewardViewModel.getReward(id.toLong())
    }
    val reward by rewardViewModel.reward.collectAsState()
    val updateSuccess by rewardViewModel.updateSuccess.collectAsState()

    val errorMsg by rewardViewModel.errorMsg.collectAsState()

    val context = LocalContext.current

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
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), // ✅ Activar scroll vertical
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Visualitzar Recompensa",
                fontSize = 36.sp,
                color = Color.Black,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFFB3E5FC), shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
                    .fillMaxWidth(0.9f)

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    reward?.let { Text(text = it.name, fontSize = 28.sp, color = Color.Black) }

                    Spacer(modifier = Modifier.height(10.dp))

                    val imageBitmap = reward?.image?.let {
                        userViewModel.base64ToBitmap(it)
                    }

                    Column {
                        imageBitmap?.let {
                            Image(
                                it, contentDescription = "imatge de recompensa",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(250.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    reward?.let {
                        Text(
                            text = "Negoci: ${it.storeName}",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                    reward?.let {
                        Text(
                            text = "Adreça: ${it.adress}",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = "Punts: " + reward?.points.toString(),
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    reward?.let {
                        Text(
                            text = "Observacions: ${it.observations}",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                    reward?.let {
                        Text(
                            text = "Descripcio: ${it.description}",
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = "Estat: " + reward?.state.toString(),
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                    if (reward?.pickupDate != null) {
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(text = "Data de recollida: ${reward?.pickupDate}",
                            fontSize = 20.sp,
                            color = Color.Black)
                    } else if (reward?.assignationDate != null) {
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(text = "Data d'assignació: ${reward?.assignationDate}",
                            fontSize = 20.sp,
                            color = Color.Black)
                    } else if (reward?.reservationDate != null) {
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(text = "Data de reserva: ${reward?.reservationDate}",
                            fontSize = 20.sp,
                            color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(text = "Data de creació: ${reward?.date}",
                        fontSize = 20.sp,
                        color = Color.Black)

                    Spacer(modifier = Modifier.height(16.dp))

                    if (reward?.state == StateType.DISPONIBLE && currentUser!!.points >= reward?.points!!) {
                        Button(
                            onClick = { rewardViewModel.reservar(reward!!, currentUser!!) },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5))
                        ) {//todo que no se cambie con error
                            Text("Reservar", fontSize = 24.sp, color = Color.DarkGray)
                        }
                    } else if (reward?.state == StateType.ASSIGNADA) {
                        Button(
                            onClick = { rewardViewModel.recollir(reward!!, currentUser!!) },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5))
                        ) {
                            Text("Recollir", fontSize = 24.sp, color = Color.DarkGray)
                        }
                    }
                }
            }
            LaunchedEffect(updateSuccess) {
                updateSuccess?.let { success ->
                    if (success) {
                        Toast.makeText(context, "Operació realitzada amb exit", Toast.LENGTH_LONG)
                            .show()
                        navController.navigate("ownRewards/" + currentUser?.email)
                    } else {
                        Toast.makeText(
                            context,
                            errorMsg, Toast.LENGTH_LONG
                        ).show()

                        id.toLongOrNull()?.let { rewardViewModel.getReward(it) }
                    }
                    rewardViewModel._updateSuccess.value = null
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        BottomNavBar(navController, userViewModel, 2)
    }
}

@Preview
@Composable
fun ViewRewardPreviewScreen() {
    val fakeNavController =
        rememberNavController() // ✅ Crear un NavController fals per la preview
    ViewRewardScreen(
        navController = fakeNavController,
        userViewModel = UserViewModel(UseCases(UserRepository()))
    )  // ✅ Passar-lo a LoginScreen
}
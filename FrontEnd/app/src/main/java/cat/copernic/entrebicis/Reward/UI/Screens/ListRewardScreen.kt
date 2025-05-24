package cat.copernic.entrebicis.Reward.UI.Screens

import MarioOlaya.Projecte4.EntreBicis.entity.Reward
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.entrebicis.Core.ui.components.BottomNavBar
import cat.copernic.entrebicis.Core.ui.components.TopBar
import cat.copernic.entrebicis.Reward.Data.RewardRepository
import cat.copernic.entrebicis.Reward.Domain.RewardUseCases
import cat.copernic.entrebicis.Reward.UI.ViewModel.RewardViewModel
import cat.copernic.entrebicis.Reward.UI.ViewModel.RewardViewModelFactory
import cat.copernic.entrebicis.Users.Data.UserRepository
import cat.copernic.entrebicis.Users.Domain.UseCases
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel

@Composable
fun ListRewardScreen(navController: NavController, userViewModel: UserViewModel) {
    val rewardUseCases = RewardUseCases(RewardRepository())

    val rewardViewModel: RewardViewModel =
        viewModel(factory = RewardViewModelFactory(rewardUseCases))

    LaunchedEffect(Unit) {
        rewardViewModel.listRewards()
    }
    val rewards by rewardViewModel.rewards.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF99CCFF))
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado
        TopBar(navController, userViewModel)

        Column(
            modifier = Modifier
                .weight(1f, fill = true)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = "Llistat de Recompenses",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "Disponibles",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.padding(16.dp))


            // Llista de recompenses
            val recompenses = rewards

            recompenses.forEach { recompensa ->
                RecompensaCard(recompensa, userViewModel, navController)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        BottomNavBar(navController, userViewModel, 2)
    }
}

@Composable
fun RecompensaCard(recompensa: Reward, userViewmodel: UserViewModel, navController: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC0D9FF)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = { navController.navigate("viewReward/${recompensa.id}") }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageBitmap = recompensa.image?.let {
                userViewmodel.base64ToBitmap(it)
            }

            Column {
                imageBitmap?.let {
                    Image(
                        it, contentDescription = "imatge de recompensa",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(72.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = recompensa.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Negoci: ${recompensa.storeName}")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Adreça: ${recompensa.adress}")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Punts: ${recompensa.points}")
                Spacer(modifier = Modifier.width(4.dp))
                val shortDescription = if (recompensa.description.length > 50)
                    recompensa.description.take(50) + "..."
                else
                    recompensa.description

                Text(text = "Descripció: $shortDescription")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Data de Creació: ${recompensa.date}")
            }
        }
    }
}

@Preview
@Composable
fun ListRewardScreenPreview() {
    val fakeNavController =
        rememberNavController() // ✅ Crear un NavController fals per la preview
    ListRewardScreen(
        navController = fakeNavController,
        userViewModel = UserViewModel(UseCases(UserRepository()))
    )  // ✅ Passar-lo a LoginScreen
}

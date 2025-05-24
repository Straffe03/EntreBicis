package cat.copernic.entrebicis.Core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cat.copernic.entrebicis.Auth.UI.Screens.PasswordRecoveryScreen
import cat.copernic.entrebicis.Reward.UI.Screens.ListRewardScreen
import cat.copernic.entrebicis.Reward.UI.Screens.OwnRewardsScreen
import cat.copernic.entrebicis.Reward.UI.Screens.ViewRewardScreen
import cat.copernic.entrebicis.Rutas.UI.Screens.ListRoutes
import cat.copernic.entrebicis.Users.UI.Screens.LoginScreen
import cat.copernic.entrebicis.Rutas.UI.Screens.MenuScreen
import cat.copernic.entrebicis.Rutas.UI.Screens.ViewRoute
import cat.copernic.entrebicis.Users.UI.Screens.ModifyUserScreen
import cat.copernic.entrebicis.Users.UI.Screens.ProfileScreen
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel


/**
 * Funció composable que defineix la navegació de l'aplicació.
 *
 * @param userViewModel El ViewModel de l'usuari.
 */
@Composable
fun AppNavigation(userViewModel: UserViewModel) {
    val navController = rememberNavController()

    //Permet la navegació i portar un historial d'aquesta
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userViewModel) }
        composable("menu") { MenuScreen(navController, userViewModel) }
        composable("profile") { ProfileScreen(navController, userViewModel) }
        composable("listRewards") { ListRewardScreen(navController, userViewModel) }
        composable("ownRewards/{userId}") { OwnRewardsScreen(navController, userViewModel) }
        composable("viewReward/{rewardId}") { ViewRewardScreen(navController, userViewModel) }
        composable("modifyUser") { ModifyUserScreen(navController, userViewModel) }
        composable("listRoutes") { ListRoutes(navController, userViewModel) }
        composable("viewRoute/{routeId}") { ViewRoute(navController, userViewModel) }
        composable("recoverPassword"){PasswordRecoveryScreen(navController)  }

    }
}
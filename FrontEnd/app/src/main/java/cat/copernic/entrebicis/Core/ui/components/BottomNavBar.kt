package cat.copernic.entrebicis.Core.ui.components

import androidx.compose.foundation.Image
import cat.copernic.entrebicis.R


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel

@Composable
fun BottomNavBar(navController: NavController, userViewModel: UserViewModel, selectedItem: Int = 0) {
    val currentUser by userViewModel.currentUser.collectAsState()

    NavigationBar (
        containerColor = Color(0xFF6246EA)
    ) {
        val icons = listOf(
            R.drawable.map,
            R.drawable.mappin,
            R.drawable.shoppingbag,
            R.drawable.shoppingcart,
            R.drawable.logout
            )

        val destinations = listOf(
            "menu",
            "listRoutes",
            "listRewards",
            "ownRewards/${currentUser?.email}",
            "logout"
        )
        val descriptions = listOf(
            "menu",
            "llistat de rutes",
            "llistat de recompenses",
            "les meves recompenses",
            "tancat sessio"
        )
        icons.forEachIndexed { index, iconRes ->
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = descriptions[index],
                        modifier = Modifier.size(32.dp)
                    )
                },
                selected = selectedItem == index,
                onClick = { if (index < destinations.size-1) {
                    navController.navigate(destinations[index])
                }
                    if (index == destinations.size-1) {
                        userViewModel.logoutUser()
                        navController.navigate("login")
                    }
                          },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.White
                ),
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
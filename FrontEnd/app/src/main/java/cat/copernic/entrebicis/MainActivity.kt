package cat.copernic.entrebicis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.entrebicis.Core.ui.navigation.AppNavigation
import cat.copernic.entrebicis.Core.ui.theme.EntreBicisTheme
import cat.copernic.entrebicis.Users.Data.UserRepository
import cat.copernic.entrebicis.Users.Domain.UseCases
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModel
import cat.copernic.entrebicis.Users.UI.ViewModel.UserViewModelFactory

/**
 * Activitat principal de l'aplicació EntreBicis.
 *
 * Aquesta activitat inicialitza el sistema de navegació i injecta el [UserViewModel] necessari.
 */
class MainActivity : ComponentActivity() {

    /**
     * Mètode d'entrada que s'executa en la creació de l'activitat.
     *
     * @param savedInstanceState Estat de l'activitat anterior (si n'hi havia).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val useCases = UseCases(UserRepository())
            val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(useCases))
            AppNavigation(userViewModel)
        }
    }
}

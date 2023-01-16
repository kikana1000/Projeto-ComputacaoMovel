import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pt.ipp.estg.doctorbrain.firestoreFun.FireBaseViewModel
import pt.ipp.estg.doctorbrain.graphs.Graph
import pt.ipp.estg.doctorbrain.graphs.GuestScreen
import pt.ipp.estg.doctorbrain.graphs.LoginGraph
import pt.ipp.estg.doctorbrain.screens.HomeScreen

/**
 * Implementação da Navegação do programa
 * @param navHostController Controller da navigation
 * @param auth autenticação da firebase
 * @param db base de dados da firebase
 */
@ExperimentalMaterialApi
@Composable
fun RootGraph(navHostController: NavHostController) {
    val auth: FirebaseAuth = Firebase.auth

    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = if (auth.currentUser != null) Graph.HOME else Graph.AUTH
    ) {
        LoginGraph(navHostController = navHostController)
        composable(Graph.GUEST) {
            GuestScreen(rememberNavController())
        }
        composable(Graph.HOME) {
            HomeScreen(rememberNavController())
        }
    }
}
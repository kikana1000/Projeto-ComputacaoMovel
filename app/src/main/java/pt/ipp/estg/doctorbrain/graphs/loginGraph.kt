package pt.ipp.estg.doctorbrain.graphs

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pt.ipp.estg.doctorbrain.screens.app.login.LoginScreen
import pt.ipp.estg.doctorbrain.screens.app.login.SignUpScreen

/**
 * Implementação da Navegação relativa ao login
 * @param navHostController Controller da navigation
 * @param auth autenticação da firebase
 * @param db base de dados da firebase
 */
@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.LoginGraph(
    navHostController: NavHostController,
) {
    navigation(
        route = Graph.AUTH,
        startDestination = Graph.LOGIN
    ) {
        composable(Graph.LOGIN) {
            LoginScreen(
                onSignUp = {
                    navHostController.navigate(Graph.SIGNUP)
                },
                onLogin = {
                    navHostController.navigate(Graph.HOME)
                }, onGuest = {
                    navHostController.navigate(Graph.GUEST)
                }
            )
        }

        composable(Graph.SIGNUP) {
            SignUpScreen(
                onSignUp = {
                    navHostController.navigate(Graph.LOGIN)
                },
            )
        }
    }
}
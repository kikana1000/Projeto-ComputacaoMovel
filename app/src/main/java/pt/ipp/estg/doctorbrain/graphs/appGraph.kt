package pt.ipp.estg.doctorbrain.graphs


import android.content.Intent
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import pt.ipp.estg.doctorbrain.MainActivity
import pt.ipp.estg.doctorbrain.screens.*
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.SchoolClassScreen
import pt.ipp.estg.doctorbrain.screens.app.login.LoginScreen
import pt.ipp.estg.doctorbrain.screens.app.questionsScreen.InfoQuestionScreen

/**
 * Implementação da navigation do programa na sua totalidade
 * @param navHostController Controller da navigation
 * @param auth autenticação da firebase
 * @param db base de dados da firebase
 */
@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun appGraph(navHostController: NavHostController) {
    val context= LocalContext.current
    val questionIdGoBack = remember { mutableStateOf("") }
    val uri="https://www.doctorbrain.com"
    NavHost(
        navController = navHostController,
        route = Graph.HOME,
        startDestination = Graph.CALENDAR
    ) {

        composable(Graph.CALENDAR) {
            CalendarScreen(navHostController,context)
        }
        composable(Graph.CLASSES) {
            SchoolClassScreen(navHostController,context)
        }
        composable(Graph.QUESTIONS) {
            QuestionsScreen(
                onGroupsList = {
                    navHostController.navigate(Graph.GROUPS)
                },
                onSelectedQuestion = { selectedQuestionID ->
                    navHostController.navigate(Graph.INFOQUESTION + "/${selectedQuestionID}")
                    questionIdGoBack.value = Graph.QUESTIONS
                })
        }
        composable(//passa o ID/Referencia da Question para q possa ser listada na InfoQuestionScreen
            Graph.INFOQUESTION + "/{selectedQuestionID}",
            arguments = listOf(navArgument("selectedQuestionID") {
                type = NavType.StringType
                defaultValue = "mySelectedQuestionID"
            }),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/selectedQuestionID={selectedQuestionID}" })
        ) {
            val selectedQuestionID: String? = it.arguments?.getString("selectedQuestionID")
            if (selectedQuestionID != null) {
                InfoQuestionScreen(selectedQuestionID,questionIdGoBack.value ,navHostController)
            }
        }
        composable(Graph.GROUPS) {
            GroupsScreen()
        }
        composable(Graph.EVENTS) {
            EventsScreen(navHostController)
        }

        composable(Graph.PROFILE) {
            ProfileScreen(onLogout = {
                navHostController.navigate(Graph.ROOT) //esta a dar erro
            },
                onSelectedQuestion = { selectedQuestionID ->
                    navHostController.navigate(Graph.INFOQUESTION + "/${selectedQuestionID}")
                    questionIdGoBack.value = Graph.PROFILE
                })
        }
        //Quando da logout, volta para o RootGraph //Solucao temporaria, mas melhor q nada
        composable(Graph.ROOT) {
                LoginScreen(
                    onSignUp = {
                        navHostController.navigate(Graph.SIGNUP)
                    },
                    onLogin = {
                        navHostController.navigate(Graph.HOME)
                    },
                    onGuest = {
                        navHostController.navigate(Graph.GUEST)
                    }
                )

        }
    }
}
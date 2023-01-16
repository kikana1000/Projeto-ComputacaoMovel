package pt.ipp.estg.doctorbrain.graphs

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import pt.ipp.estg.doctorbrain.MainActivity
import pt.ipp.estg.doctorbrain.screens.*
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.SchoolClassScreen
import pt.ipp.estg.doctorbrain.screens.app.login.LoginScreen
import pt.ipp.estg.doctorbrain.screens.app.questionsScreen.InfoQuestionScreen

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun GuestScreen(navController: NavHostController){
    Scaffold(
        bottomBar = {BottomBar(navController)}) {
            innerPadding ->
        // Apply the padding globally to the whole BottomNavScreensController
        Box(modifier = Modifier.padding(innerPadding)) {
            guestGraph(navHostController = navController)
        }

    }
}
@Composable
fun BottomBar(navController: NavHostController) {
    val barOptions = listOf<BarOptions>(
        BarOptions.Calendar,
        BarOptions.Events
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        barOptions.forEach { option ->
            AddItem(
                screen = option,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun guestGraph (navHostController: NavHostController) {
    val context= LocalContext.current
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

        composable(Graph.EVENTS) {
            EventsScreen(navHostController)
        }

    }
}



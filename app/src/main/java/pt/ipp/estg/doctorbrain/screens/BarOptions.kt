package pt.ipp.estg.doctorbrain.screens

import androidx.compose.material.icons.*;
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BarOptions(
    val title: String,
    val route: String,
    val icon: ImageVector
) {

    object Calendar : BarOptions(
        title = "Calendar",
        route = "www.doctorbrain.com/calendar",
        icon = Icons.Default.CalendarToday
    )

    object Questions : BarOptions(
        title = "Questions",
        route = "www.doctorbrain.com/questions",
        icon = Icons.Default.Help
    )

    object Events : BarOptions(
        title = "Events",
        route = "www.doctorbrain.com/events",
        icon = Icons.Default.Verified
    )

    object Logout : BarOptions(
        title = "Logout",
        route = "www.doctorbrain.com/root",
        icon = Icons.Default.Logout
    )

    object User : BarOptions(
        title = "Profile",
        route = "www.doctorbrain.com/profile",
        icon = Icons.Default.AccountCircle
    )
}
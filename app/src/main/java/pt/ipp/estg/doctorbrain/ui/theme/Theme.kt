package pt.ipp.estg.doctorbrain.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Definição do tema do programa
 */
private val DarkColorPalette = darkColors(
    primary = RaisinBlack,
    primaryVariant = Xiketic,
    secondary = DarkLiver
)

private val LightColorPalette = lightColors(
    primary = primary,
    primaryVariant = primaryVariant,
    background = background,
    secondary = secondary,
)

@Composable
fun DoctorBrainTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = primary,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color =primary,
            darkIcons = false
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
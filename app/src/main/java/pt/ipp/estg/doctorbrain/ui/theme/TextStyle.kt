package pt.ipp.estg.doctorbrain.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Definição de tipo de letras para usar no programa
 */
//Titles on top of Screens
fun TitleofScreen(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,)
}

//CardTitles
fun InputLabel():TextStyle{
    return TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,)
}

//CardTitles
fun CardTitle():TextStyle{
    return TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,)
}


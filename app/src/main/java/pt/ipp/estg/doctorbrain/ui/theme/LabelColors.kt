package pt.ipp.estg.doctorbrain.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Definição de cores para as label para usar no programa
 */
sealed class LabelColors(
    val title:String,
    val color:Color
){

    object red:LabelColors(
        title = "Red",
        color = Color(0xFFB76D68)
    )

    object lightGreen:LabelColors(
        title = "LightGreen",
        color = Color(0xFF22A39F)
    )

    object green:LabelColors(
        title = "LightGreen",
        color = Color(0xFF254D4C)
    )

    object yellow:LabelColors(
        title = "Yellow",
        color = Color(0xFFC0BF26)
    )
    object blue:LabelColors(
        title = "Blue",
        color = Color(0xFF5B63B7)
    )


}

fun getAllLabelColor():List<LabelColors>{
    return listOf<LabelColors>(
        LabelColors.red, LabelColors.lightGreen,
        LabelColors.green,
        LabelColors.yellow,
        LabelColors.blue
    )
}

fun getLabelColorByName(name:String?): Color {
    if (name == null) return Color.White
    getAllLabelColor().forEach {
        if (it.title.equals(name)) return it.color
    }
    return Color.White
}

fun getLabelColorObjectByName(name:String?): LabelColors {
    getAllLabelColor().forEach {
        if (it.title == name) return it
    }
    return LabelColors.red
}

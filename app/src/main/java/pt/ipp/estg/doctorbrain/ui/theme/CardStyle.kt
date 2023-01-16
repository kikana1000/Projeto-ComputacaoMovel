package pt.ipp.estg.doctorbrain.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


fun cardStyleModifier(): Modifier {
    return (Modifier.fillMaxWidth().padding(bottom = 10.dp))
}

fun cardStyleBorder(): BorderStroke {
    return BorderStroke(2.dp, DarkBlack)
}

fun cardStyleBorder(bordercolor: Color): BorderStroke {
    return BorderStroke(2.dp, bordercolor)
}
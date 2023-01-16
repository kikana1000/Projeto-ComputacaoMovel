package pt.ipp.estg.doctorbrain.screens.app.calendarScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Aplica uma row para fazer o display de uma Semana
 *
 * @param weekday @param[weekday] Dia da semana a dar Display
« */
@Composable
fun WeekInfo(weekday:String){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .padding(5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Day: $weekday")
    }
}

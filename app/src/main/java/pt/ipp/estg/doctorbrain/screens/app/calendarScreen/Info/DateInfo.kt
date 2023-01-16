package pt.ipp.estg.doctorbrain.screens.app.calendarScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Aplica uma row para fazer o display de uma Hora
 *
 * @param label @param[label] Mensagem a aparecer antes da hora (Start/Finish)
 * @param hour @param[label] Hora a fazer display
 */
@Composable
fun DateInfo(label: String, hour: String){
    Row() {
        Text(text = "$label:", Modifier.padding(15.dp))
        Button(onClick = {}) {
            Text(text = hour)
        }
    }
}
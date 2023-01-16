package pt.ipp.estg.doctorbrain.screens.sytemInputs

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipp.estg.doctorbrain.ui.theme.cardStyleModifier
import java.time.LocalTime

/**
 *  UI de seleção de uma hora de inicio e fim
 *
 *  @param context
 *  @param returnStartTime @param[returnStartTime] função para retornar a informação depois de selecionada
 *  @param returnFinishTime @param[returnFinishTime] função para retornar a informação depois de selecionada
 */
@Composable
fun StartFinishTimePicker(
    context: Context,
    returnStartTime: (String) -> Unit,
    returnFinishTime: (String) -> Unit
) {

    Card(modifier = cardStyleModifier(), elevation = 10.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth(), horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val hour = LocalTime.now().hour
                val minute = LocalTime.now().minute
                val sttime = "$hour:$minute"
                val fstime = "$hour:$minute"
                var stHour = remember { mutableStateOf(sttime) }

                var fsHour = remember { mutableStateOf(fstime) }
                SelectTimeStart( context,fsHour.value) {
                    stHour.value=it
                    returnStartTime(it)
                }

                SelectTimeFinish(context,stHour.value) {
                    fsHour.value=it
                    returnFinishTime(it)
                }
            }
        }
    }
}
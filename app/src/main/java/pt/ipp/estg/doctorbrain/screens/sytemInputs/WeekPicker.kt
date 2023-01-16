package pt.ipp.estg.doctorbrain.screens.sytemInputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipp.estg.doctorbrain.models.enums.WeekDays
import pt.ipp.estg.doctorbrain.models.enums.getWeekDayByInt
import java.util.*


/**
 *  UI de seleção de um dia da semana
 *  @param weekDay @param[weekDay] valor inicial para o weekDay
 *  @param returnWeek @param[returnWeek] função para retornar a informação depois de selecionada
 */
@Composable
fun WeekPicker(weekDay:String,returnWeek:(String)->Unit){
    val weekday = remember { mutableStateOf(WeekDays.valueOf(weekDay)) }
    returnWeek(weekday.value.name)
    var expanded: Boolean by remember { mutableStateOf(false) }
    Row(modifier = Modifier
        .clickable { expanded = true }
        .fillMaxWidth()
        .height(40.dp)
        .padding(5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Day: ${weekday.value}")
        Icon(Icons.Default.ExpandMore, null)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            WeekDays.values().forEach { item ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    weekday.value = item
                    returnWeek(item.name)
                }) {
                    Text(text = item.name)
                }
            }
        }
    }
}
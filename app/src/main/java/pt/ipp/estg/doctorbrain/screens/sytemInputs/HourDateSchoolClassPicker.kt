package pt.ipp.estg.doctorbrain.screens.app.calendarScreen

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
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass
import pt.ipp.estg.doctorbrain.models.enums.WeekDays
import pt.ipp.estg.doctorbrain.models.enums.getWeekDayByInt
import pt.ipp.estg.doctorbrain.screens.sytemInputs.SelectTimeFinish
import pt.ipp.estg.doctorbrain.screens.sytemInputs.SelectTimeStart
import pt.ipp.estg.doctorbrain.screens.sytemInputs.WeekPicker
import pt.ipp.estg.doctorbrain.ui.theme.cardStyleModifier
import java.util.*

/**
 *  UI de seleção de uma HourDateSchoolClass
 *
 *  @param context
 *  @param hourDate @param[hourDate] HourDateSchoolClass para editar
 *  @param returnHourDate @param[returnHourDate] função para retornar a informação depois de selecionada
 */
@Composable
fun HourDateSchoolClassPicker(
    context: Context,
    hourDate: HourDateSchoolClass,
    returnHourDate: (HourDateSchoolClass) -> Unit
) {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()
    Card(modifier = cardStyleModifier(), elevation = 10.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth(), horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                var stHour = remember { mutableStateOf(hourDate.startHour) }
                var fsHour = remember { mutableStateOf(hourDate.finishtHour) }
                SelectTimeStart(context,fsHour.value) {
                    hourDate.startHour = it
                    stHour.value=it
                    returnHourDate(hourDate)
                }

                SelectTimeFinish(context,stHour.value) {
                    hourDate.finishtHour = it
                    fsHour.value=it
                    returnHourDate(hourDate)
                }
            }
            WeekPicker(getWeekDayByInt(calendar.get(Calendar.DAY_OF_WEEK)).toString(),returnWeek = {
                hourDate.weekDays = it
                returnHourDate(hourDate)
            })
        }
    }
}

@Composable
fun HourDateSchoolClassPicker(
    startTime:String,
    finishTime:String,
    weekDay:String,
    context: Context,
    hourDate: HourDateSchoolClass,
    returnHourDate: (HourDateSchoolClass) -> Unit
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
                var stHour = remember { mutableStateOf(hourDate.startHour) }
                var fsHour = remember { mutableStateOf(hourDate.finishtHour) }
                SelectTimeStart(startTime, context,fsHour.value) {
                    hourDate.startHour = it
                    stHour.value=it
                    returnHourDate(hourDate)
                }

                SelectTimeFinish(finishTime, context,stHour.value) {
                    hourDate.finishtHour = it
                    fsHour.value=it
                    returnHourDate(hourDate)
                }
            }
            WeekPicker(weekDay,returnWeek = {
                hourDate.weekDays = it
                returnHourDate(hourDate)
            })
        }
    }
}
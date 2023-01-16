package pt.ipp.estg.doctorbrain.screens.app.calendarScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass
import pt.ipp.estg.doctorbrain.ui.theme.cardStyleModifier
import pt.ipp.estg.doctorbrain.ui.theme.getLabelColorByName

/**
 * HourCard
 *
 * aplica a view de Card a uma HourDateSchoolClass dada
 *
 * @param hourdate @param[hourdate]
 * */
@Composable
fun HourWeekCard(hourdate: HourDateSchoolClass) {
    Card(
        modifier = cardStyleModifier(),
        elevation = 10.dp,) {
        Column(
            modifier = Modifier
                .fillMaxWidth(), horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                DateInfo("Start", hourdate.startHour)
                DateInfo("Finish", hourdate.finishtHour)
            }
            if (hourdate.weekDays != "null") {
                WeekInfo(weekday = hourdate.weekDays)
            }

        }
    }
}



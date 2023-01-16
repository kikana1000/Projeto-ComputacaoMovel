package pt.ipp.estg.doctorbrain.screens.app.eventsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipp.estg.doctorbrain.models.SingleEvent
import pt.ipp.estg.doctorbrain.ui.theme.*

/**
 * Card que contêm informações relativa a um Evento
 *
 * @param sEvent Evento que irá demonstrar a informação
 * @param moreInfo
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SingleEventCard(sEvent: SingleEvent, moreInfo: (SingleEvent) -> Unit, icon: Boolean) {
    Card(
        onClick = {
            moreInfo(sEvent)
        },
        modifier = cardStyleModifier(),
        backgroundColor = getLabelColorByName(sEvent.color),
        contentColor = Color.White,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Row {
                    if (icon) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Text(text = sEvent.title, style = InputLabel())
                }
                Text(text = "Local: ${sEvent.local}")
                Text(text = "${sEvent.date}")
            }
            Column(
                modifier = Modifier.padding(end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${sEvent.startHour} ", style = TitleofScreen())
                Text(text = "${sEvent.finishtHour}", style = TitleofScreen())
            }
        }
    }
}

//WithOutWeekDay
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SingleEventCardWithoutWeekDay(sEvent: SingleEvent, moreInfo: (SingleEvent) -> Unit) {
    Card(
        onClick = {
            moreInfo(sEvent)
        },
        modifier = cardStyleModifier(),
        backgroundColor = getLabelColorByName(sEvent.color),
        contentColor = Color.White
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(text = sEvent.title, style = InputLabel())
                Text(text = "Local: ${sEvent.local}")
            }
            Column(
                modifier = Modifier.padding(end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${sEvent.startHour}", style = TitleofScreen())
                Text(text = "${sEvent.finishtHour}", style = TitleofScreen())
            }
        }
    }
}

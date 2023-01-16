package pt.ipp.estg.doctorbrain.screens.app.calendarScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen
import pt.ipp.estg.doctorbrain.ui.theme.cardStyleModifier
import pt.ipp.estg.doctorbrain.ui.theme.getLabelColorByName

/**
 * SchoolCard
 *
 * aplica a view de Card a uma SchoolClass dada, onclick retorna a mesma SchoolClass
 *
 * @param sclass @param[sclass] SchoolClass a fazer display
 * @param moreInfo @param[moreInfo]
 * */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SchoolCard(sclass: SchoolClass, moreInfo: (SchoolClass) -> Unit) {
    Card(
        onClick = {
            moreInfo(sclass)
        },
        modifier = cardStyleModifier(),
        backgroundColor = getLabelColorByName(sclass.color),
        contentColor = Color.White,
    ) {
        Row() {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = sclass.title, style = InputLabel())
                Text(text = "Local: ${sclass.local}")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SchoolCard(
    sclass: SchoolClass,
    startHour: String,
    finishHour: String,
    moreInfo: (SchoolClass) -> Unit
) {
    Card(
        onClick = {
            moreInfo(sclass)
        },
        modifier = cardStyleModifier(),
        backgroundColor = getLabelColorByName(sclass.color),
        contentColor = Color.White,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Row() {
                    Icon(Icons.Default.School, contentDescription = "", modifier = Modifier.padding(end = 8.dp))
                    Text(text = sclass.title, style = InputLabel())
                }
                Text(text = "Local: ${sclass.local}")
            }
            Column(
                modifier = Modifier.padding(end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = startHour, style = TitleofScreen())
                Text(text = finishHour, style = TitleofScreen())
            }
        }

    }
}

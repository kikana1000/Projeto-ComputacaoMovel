package pt.ipp.estg.doctorbrain.screens.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass
import pt.ipp.estg.doctorbrain.models.SingleEvent
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.HourWeekCard
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.getLabelColorByName

@Composable
fun InfoSingleEventContent(singleEvent: SingleEvent) {

    //Local
    Text(
        text = "Local:", style = InputLabel(),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Left
    )
    Text(
        text = singleEvent.local,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Left
    )


    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = "Event Hour:", style = InputLabel())
    }
    HourWeekCard(
        hourdate = HourDateSchoolClass(
            -1,
            singleEvent.startHour,
            singleEvent.finishtHour,
            singleEvent.date,
            true
        )
    )

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = "Color:", style = InputLabel())
    }

    Button(
        onClick = {},
        modifier = Modifier.size(50.dp),  //avoid the oval shape
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = getLabelColorByName(singleEvent.color)
        )
    ) {}
}
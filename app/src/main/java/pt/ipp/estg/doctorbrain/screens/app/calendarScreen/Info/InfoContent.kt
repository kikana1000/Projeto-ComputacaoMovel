package pt.ipp.estg.doctorbrain.screens.app.calendarScreen.Info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.doctorbrain.data.SchoolClassViewModel
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.HourWeekCard
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.getLabelColorByName

/**
 *
 * Gera o conteudo da pagina de IndoClassBottomSheetContent
 *
 * @param schoolClass param[schoolClass] SchoolClass a fazer display
 * */
@Composable
fun InfoContent(schoolClass: SchoolClass) {
    val schoolClassViewModel: SchoolClassViewModel = viewModel()
    val hourList = schoolClass.let { schoolClassViewModel.getHourDateBySchooClass(it._eventID).observeAsState() }
    Text(
        text = "Local: ${schoolClass.local}", style = InputLabel(),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Left
    )

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = "Class Hour:", style = InputLabel())
    }
    hourList.value?.forEach { c ->
        HourWeekCard(c)
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = "Color:", style = InputLabel())
    }

        Button(
            onClick = {},
            modifier = Modifier.size(50.dp),  //avoid the oval shape
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = getLabelColorByName(schoolClass.color)
            )
        ) {}
}

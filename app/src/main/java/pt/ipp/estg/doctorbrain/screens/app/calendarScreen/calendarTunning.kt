package pt.ipp.estg.doctorbrain.screens.app.calendarScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mabn.calendarlibrary.ExpandableCalendar
import com.mabn.calendarlibrary.core.calendarDefaultTheme
import pt.ipp.estg.doctorbrain.ui.theme.*
import java.time.LocalDate

/**
 * Elemento que demonstra a API do calendario implementada
 * @param returnDate data selecionada que retorna do calendario
 */
@Composable
fun CalendarTunning(returnDate:(LocalDate) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(primary),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        //Calendar Text
        Text(
            text = "Calendar",
            style = TitleofScreen(),
            color = BeigeWhite2,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
    ExpandableCalendar(theme = calendarDefaultTheme.copy(
        backgroundColor = primary,
        headerBackgroundColor = primary,
        dayBackgroundColor= primary,
        selectedDayBackgroundColor= BeigeWhite2,
        dayValueTextColor= BeigeWhite2,
        selectedDayValueTextColor=DarkBlack,
        headerTextColor= BeigeWhite3,
        weekDaysTextColor=BeigeWhite3,
        dayShape = RoundedCornerShape(50.dp)
    ), onDayClick = {
        returnDate(it)
    })
}
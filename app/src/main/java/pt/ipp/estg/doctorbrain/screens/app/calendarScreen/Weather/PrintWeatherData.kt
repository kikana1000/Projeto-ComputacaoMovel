package pt.ipp.estg.doctorbrain.screens.app.calendarScreen.Weather

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipp.estg.doctorbrain.models.IpmaObject
import pt.ipp.estg.doctorbrain.models.WeatherTypeObject
import pt.ipp.estg.doctorbrain.ui.theme.CardTitle

/**
 * Display do Card com informações climatéricas
 */
@Composable
fun PrintWeatherData(weatherObject: IpmaObject?, weatherTypeObject: WeatherTypeObject?) {
    if (weatherObject != null && weatherTypeObject != null) {
        val weatherDataOfToday = weatherObject.data.first()
        var weatherTypeDesc = ""
        for (weatherType in weatherTypeObject.data) {
            if (weatherType.idWeatherType == weatherDataOfToday.idWeatherType) {
                weatherTypeDesc = weatherType.descWeatherTypePT
            }
        }
        Card() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tempo: ${weatherDataOfToday.tMin}ºC / ${weatherDataOfToday.tMax}ºC",
                    style = CardTitle()
                )

                Icon(
                    GetWeatherIcon(weatherDataOfToday.idWeatherType)!!,
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

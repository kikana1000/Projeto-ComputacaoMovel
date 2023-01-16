package pt.ipp.estg.doctorbrain.screens.app.calendarScreen.Weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import pt.ipp.estg.doctorbrain.R

/**
 * Retorna o Icon do Weather baseado no ID retirado do weatherDataOfToday.idWeatherType
 * @param weatherTypeDesc @para[weatherTypeDesc]  ID do tipo de weatherDataOfToday
 */
@Composable
fun GetWeatherIcon(weatherTypeDesc: Int): Painter? {
    when (weatherTypeDesc) {
        1 -> return painterResource(id = R.drawable.w_ic_d_01)
        2 -> return painterResource(id = R.drawable.w_ic_d_02)
        3 -> return painterResource(id = R.drawable.w_ic_d_03)
        4 -> return painterResource(id = R.drawable.w_ic_d_04)
        5 -> return painterResource(id = R.drawable.w_ic_d_05)
        6 -> return painterResource(id = R.drawable.w_ic_d_06)
        7 -> return painterResource(id = R.drawable.w_ic_d_07)
        8 -> return painterResource(id = R.drawable.w_ic_d_08)
        9 -> return painterResource(id = R.drawable.w_ic_d_09)
        10 -> return painterResource(id = R.drawable.w_ic_d_10)
        11 -> return painterResource(id = R.drawable.w_ic_d_11)
        12 -> return painterResource(id = R.drawable.w_ic_d_12)
        13 -> return painterResource(id = R.drawable.w_ic_d_13)
        14 -> return painterResource(id = R.drawable.w_ic_d_14)
        15 -> return painterResource(id = R.drawable.w_ic_d_15)
        16 -> return painterResource(id = R.drawable.w_ic_d_16)
        17 -> return painterResource(id = R.drawable.w_ic_d_17)
        18 -> return painterResource(id = R.drawable.w_ic_d_18)
        19 -> return painterResource(id = R.drawable.w_ic_d_19)
        20 -> return painterResource(id = R.drawable.w_ic_d_20)
        21 -> return painterResource(id = R.drawable.w_ic_d_21)
        22 -> return painterResource(id = R.drawable.w_ic_d_22)
        23 -> return painterResource(id = R.drawable.w_ic_d_23)
        24 -> return painterResource(id = R.drawable.w_ic_d_24)
        25 -> return painterResource(id = R.drawable.w_ic_d_25)
        26 -> return painterResource(id = R.drawable.w_ic_d_26)
        27 -> return painterResource(id = R.drawable.w_ic_d_27)
        28 -> return painterResource(id = R.drawable.w_ic_d_28)
        29 -> return painterResource(id = R.drawable.w_ic_d_29)
        30 -> return painterResource(id = R.drawable.w_ic_d_30)

    }
    return null
}
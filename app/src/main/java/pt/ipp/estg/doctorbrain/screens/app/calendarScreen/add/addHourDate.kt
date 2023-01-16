package pt.ipp.estg.doctorbrain.screens.app.calendarScreen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass
import pt.ipp.estg.doctorbrain.models.enums.WeekDays
import java.time.LocalTime
import java.util.*

/**
 *  UI de seleção de 1..3 HourDateSchoolClass
 *
 *  @param context
 *  @param returnlistofHourDate @param[returnlistofHourDate] retorna a lista de HourDateSchoolClass Selecionada
 *  @param returnCount @param[returnCount] retorna a quantidade de returnlistofHourDate a guardar
 */
@Composable
fun GetListofHourDate(
    context: Context,
    returnlistofHourDate: (List<HourDateSchoolClass>) -> Unit,
    returnCount: (Int) -> Unit
) {
    val hour = LocalTime.now().hour
    val minute = LocalTime.now().minute
    val time = "$hour:$minute"
    val count = remember {
        mutableStateOf(0)
    }
    val list =
        remember { mutableListOf(
            HourDateSchoolClass(0, time, time, WeekDays.MONDAY.name,true)
            ,
            HourDateSchoolClass(0, time, time, WeekDays.MONDAY.name,true),
            HourDateSchoolClass(0, time, time, WeekDays.MONDAY.name,true)) }

    for (i in 0..count.value) {
        HourDateSchoolClassPicker(context, list[i], returnHourDate = {
            list[i] = it
            returnlistofHourDate(list.toList())
            returnCount(count.value)
        }
        )
    }

    Row {
        if (count.value == 0 || count.value == 1) {
            IconButton(onClick = {
                returnlistofHourDate(list.toList())
                returnCount(count.value)
                if (count.value < 2) {
                    count.value++
                } else {
                    Toast.makeText(context, "Max Hour Date is 3", Toast.LENGTH_SHORT).show()
                }
            }) {
                Icon(Icons.Default.Add, "")
            }
        }
        if (count.value == 1 || count.value == 2) {
            IconButton(onClick = {
                returnlistofHourDate(list.toList())
                returnCount(count.value)
                if (count.value > 0) {
                    count.value--
                } else {
                    Toast.makeText(context, "Min Hour Date is 1", Toast.LENGTH_SHORT).show()
                }
            }) {
                Icon(Icons.Default.Delete, "")
            }
        }
    }
}

@Composable
fun GetListofHourDate(
    context: Context,
    listOriginal:List<HourDateSchoolClass>,
    returnlistofHourDate: (List<HourDateSchoolClass>) -> Unit,
    returnCount: (Int) -> Unit
) {
    val hour = LocalTime.now().hour
    val minute = LocalTime.now().minute
    val time = "$hour:$minute"
    val count = remember {
        mutableStateOf(listOriginal.size-1)
    }
    val list = listOriginal.toMutableList()
    Log.d("List[GetListofHourDate]",list.size.toString().plus(list.toString()))

    while (list.size<3){
        list.add(HourDateSchoolClass(-1, time, time, WeekDays.MONDAY.name,true))
    }

    for (i in 0..count.value) {
        HourDateSchoolClassPicker(list[i].startHour,list[i].finishtHour,list[i].weekDays,context, list[i], returnHourDate = {
            list[i] = it
            returnlistofHourDate(list.toList())
            returnCount(count.value)
        }
        )
    }

    Row {
        if (count.value == 0 || count.value == 1) {
            IconButton(onClick = {
                if (count.value < 2) {
                    count.value++
                } else {
                    Toast.makeText(context, "Max Hour Date is 3", Toast.LENGTH_SHORT).show()
                }
                returnlistofHourDate(list.toList())
                returnCount(count.value)
            }) {
                Icon(Icons.Default.Add, "")
            }
        }
        if (count.value == 1 || count.value == 2) {
            IconButton(onClick = {
                if (count.value > 0) {
                    count.value--
                } else {
                    Toast.makeText(context, "Min Hour Date is 1", Toast.LENGTH_SHORT).show()
                }
                returnlistofHourDate(list.toList())
                returnCount(count.value)
            }) {
                Icon(Icons.Default.Delete, "")
            }
        }
    }
}
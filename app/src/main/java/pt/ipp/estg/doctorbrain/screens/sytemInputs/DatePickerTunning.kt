package pt.ipp.estg.doctorbrain.screens.sytemInputs

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import java.util.*

/**
 * Inicia um Android Date Picker Dialog e retorna a Data selecionada em formato de String
 * @param returnDate @param[returnDate] returns date selected in string Format
 */
@Composable
fun DatePickerTunning(returnDate:(String)->Unit){
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) +1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val date = remember { mutableStateOf(day.toString().plus("/").plus(month).plus("/").plus(year)) }
    //Depois de inicializado retorna a data para inicializar a variavel enviada pelo returnDate
    returnDate(date.value)
    val datepickerdialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            date.value = "$mDayOfMonth/${mMonth+1}/$mYear"
            returnDate(date.value)
        }, year, month-1, day
    )
    // selecionar Data minima
    datepickerdialog.datePicker.minDate = calendar.timeInMillis

    Column(modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        ) {
        Text(text = "Date", style = InputLabel())
        Button(onClick = {
            datepickerdialog.show()
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = date.value,color =Color.White)
        }
    }
}

@Composable
fun DatePickerTunning(returnDate:(String)->Unit,inicialdate:String){
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val date = remember { mutableStateOf(inicialdate) }
    //Depois de inicializado retorna a data para inicializar a variavel enviada pelo returnDate
    returnDate(date.value)
    val datepickerdialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            date.value = "$mDayOfMonth/${mMonth+1}/$mYear"
            returnDate(date.value)
        }, year, month, day
    )
    // selecionar Data minima
    datepickerdialog.datePicker.minDate = calendar.timeInMillis

    Column(modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Date", style = InputLabel())
        Button(onClick = {
            datepickerdialog.show()
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = date.value,color = Color.White)
        }
    }
}
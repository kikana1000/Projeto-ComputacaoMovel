package pt.ipp.estg.doctorbrain.screens.sytemInputs

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalTime

/**
 *  UI de seleção de uma hora
 *
 *  @param context
 *  @param label texto para apresentar com o picker
 *  @param returnTime @param[returnTime] função para retornar a informação depois de selecionada
 */
@Composable
fun SelectTimeStart( context: Context, max:String, returnTime: (String) -> Unit) {

    var fminute = LocalTime.now().minute.toString()
    var fhour = LocalTime.now().hour.toString()
    if(fminute.toInt()<10){
        fminute=("0".plus(fminute))
    }
    if(fhour.toInt()<10){
        fhour=("0".plus(fhour))
    }
    val maxHour = Integer.parseInt(max.split(":")[0])
    val maxMinu = Integer.parseInt(max.split(":")[1])
    val time = remember { mutableStateOf("$fhour:$fminute") }
    val timeHour= Integer.parseInt(time.value.split(":")[0])
    val timeMin= Integer.parseInt(time.value.split(":")[1])
    returnTime(time.value)
    val timePickerDialog = TimePickerDialog(
        context,2,
        { _, mHour: Int, mMinute: Int ->
            var minute=mMinute.toString()
            var hour=mHour.toString()
            if(mMinute<10){
                minute="0".plus(minute)
            }
            if(mHour<10){
                hour="0".plus(hour)
            }
            time.value = "$hour:$minute"
            if(((mHour*60)+mMinute)>((maxHour*60)+maxMinu)) {
                Toast.makeText(context, "Start has to be lower then the Finish", Toast.LENGTH_SHORT).show()
                time.value=max
            }
            returnTime(time.value)
        }, timeHour, timeMin,true
    )
    Row() {
        Text(text = "Start:", Modifier.padding(15.dp))
        Button(onClick = {
            timePickerDialog.show()
        }) {
            Text(text = time.value,color= Color.White)
        }
    }
}
@Composable
fun SelectTimeFinish( context: Context, min:String, returnTime: (String) -> Unit) {

    var fminute = LocalTime.now().minute.toString()

    val minHour = Integer.parseInt(min.split(":")[0])
    val minMinu = Integer.parseInt(min.split(":")[1])
    var fhour = (LocalTime.now().hour ).toString()
    if(fminute.toInt()<10){
        fminute=("0".plus(fminute))
    }
    if(fhour.toInt()<10){
        fhour=("0".plus(fhour))
    }
    val time = remember { mutableStateOf("$fhour:$fminute") }
    val timeHour= Integer.parseInt(time.value.split(":")[0])
    val timeMin= Integer.parseInt(time.value.split(":")[1])
    returnTime(time.value)
    val timePickerDialog = TimePickerDialog(
        context,2,
        { _, mHour: Int, mMinute: Int ->
            var minute=mMinute.toString()
            var hour=mHour.toString()
            if(mMinute<10){
                minute="0".plus(minute)
            }
            if(mHour<10){
                hour="0".plus(hour)
            }
            time.value = "$hour:$minute"
            if(((mHour*60)+mMinute)<((minHour*60)+minMinu)){
                Toast.makeText(context, "Finish has to be bigger then the Start", Toast.LENGTH_SHORT).show()
                time.value=min
            }
            returnTime(time.value)
        }, timeHour, timeMin,true

    )
    Row() {
        Text(text = "Finish:", Modifier.padding(15.dp))
        Button(onClick = {
            timePickerDialog.show()
        }) {
            Text(text = time.value,color= Color.White)
        }
    }
}
@Composable
fun SelectTimeStart(time:String, context: Context, max:String, returnTime: (String) -> Unit) {

    val maxHour = Integer.parseInt(max.split(":")[0])
    val maxMinu = Integer.parseInt(max.split(":")[1])
    val time = remember { mutableStateOf(time) }
    val timeHour= Integer.parseInt(time.value.split(":")[0])
    val timeMin= Integer.parseInt(time.value.split(":")[1])
    val timePickerDialog = TimePickerDialog(
        context,2,
        { _, mHour: Int, mMinute: Int ->
            var minute=mMinute.toString()
            var hour=mHour.toString()
            if(mMinute<10){
                minute="0".plus(minute)
            }
            if(mHour<10){
                hour="0".plus(hour)
            }
            time.value = "$hour:$minute"
            if(((mHour*60)+mMinute)>((maxHour*60)+maxMinu)){
                Toast.makeText(context, "Start has to be lower then the Finish", Toast.LENGTH_SHORT).show()
                time.value=max
            }
            returnTime(time.value)
        }, timeHour, timeMin, true
    )
    Row() {
        Text(text = "Start:", Modifier.padding(15.dp))
        Button(onClick = {
            timePickerDialog.show()
        }) {
            Text(text = time.value,color= Color.White)
        }
    }
}

@Composable
fun SelectTimeFinish(time:String, context: Context, min:String ,returnTime: (String) -> Unit) {
    val time = remember { mutableStateOf(time) }
    val timeHour= Integer.parseInt(time.value.split(":")[0])
    val timeMin= Integer.parseInt(time.value.split(":")[1])
    val minHour = Integer.parseInt(min.split(":")[0])
    val minMinu = Integer.parseInt(min.split(":")[1])
    val timePickerDialog = TimePickerDialog(
        context,2,
        { _, mHour: Int, mMinute: Int ->
            var minute=mMinute.toString()
            var hour=mHour.toString()
            if(mMinute<10){
                minute="0".plus(minute)
            }
            if(mHour<10){
                hour="0".plus(hour)
            }
            time.value = "$hour:$minute"
            if(((mHour*60)+mMinute)<((minHour*60)+minMinu)) {
                Toast.makeText(context, "Finish has to be bigger then the Start", Toast.LENGTH_SHORT).show()
                time.value=min
            }
            returnTime(time.value)
        },timeHour, timeMin, true
    )
    Row() {
        Text(text = "Finish:", Modifier.padding(15.dp))
        Button(onClick = {
            timePickerDialog.show()
        }) {
            Text(text = time.value,color= Color.White)
        }
    }
}

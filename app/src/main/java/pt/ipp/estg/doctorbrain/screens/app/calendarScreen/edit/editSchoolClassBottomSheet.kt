package pt.ipp.estg.doctorbrain.screens.app.calendarScreen.edit

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.doctorbrain.App
import pt.ipp.estg.doctorbrain.data.SchoolClassViewModel
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.models.enums.Local
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.GetListofHourDate
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.getAllLabelColor
import pt.ipp.estg.doctorbrain.ui.theme.getLabelColorObjectByName

@Composable
fun EditSchoolClassContent(
    schoolClass: SchoolClass,
    refresh: () -> Unit
) {
    val context = LocalContext.current
    val schoolClassViewModel: SchoolClassViewModel = viewModel()
    val hourList = schoolClass.let {
        schoolClassViewModel.getHourDateBySchooClass(it._eventID).observeAsState()
    }
    Text(
        text = "Local:", style = InputLabel(),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Left
    )
    val expanded = remember { mutableStateOf(false) }
    val local = remember { mutableStateOf(schoolClass.local) }
    Row(modifier = Modifier
        .clickable { expanded.value = true }
        .fillMaxWidth()
        .height(40.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Local: ${local.value}")
        Icon(Icons.Default.ExpandMore, null)
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            Local.values().forEach { item ->
                DropdownMenuItem(onClick = {
                    expanded.value = false
                    local.value = item.toString()
                }) {

                    Text(text = item.name)
                }

            }
        }
    }


    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = "Class Hour:", style = InputLabel())
    }

    val newList = remember { mutableStateOf(emptyList<HourDateSchoolClass>()) }
    val count = remember { mutableStateOf(hourList.value?.size ?: 0) }
    hourList.value?.let {
        GetListofHourDate(context, it, {
            newList.value = it
        }, {
            count.value = it
            for (i in 0..count.value) {
                newList.value[i].schooClass = schoolClass._eventID
            }
        })
    }


    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = "Color:", style = InputLabel())
    }
    val colors = getAllLabelColor()

    val selectedColor =
        remember { mutableStateOf(getLabelColorObjectByName(schoolClass.color)) }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(colors) { color ->
            Button(
                onClick = {
                    selectedColor.value = color
                },
                modifier = Modifier.size(50.dp),  //avoid the oval shape
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = color.color
                )
            ) {
                if (color == selectedColor.value) {
                    Icon(
                        Icons.Default.Done,
                        contentDescription = "Selected",
                        tint = Color.White
                    )
                }
            }
        }
    }

    Button(
        onClick = {
            schoolClass.local = local.value
            schoolClass.color = selectedColor.value.title
            schoolClassViewModel.update(schoolClass, newList.value, count.value, hourList.value!!)
            for (a in 0..count.value){
                newList.value[a].cancelSchedule(App.ctx!!)
                newList.value[a].schedule(App.ctx!!, schoolClass.title)
            }
            refresh()
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        elevation = ButtonDefaults.elevation(defaultElevation = 10.dp)
    ) {
        Text(
            text = "Update", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold
        )
    }
}

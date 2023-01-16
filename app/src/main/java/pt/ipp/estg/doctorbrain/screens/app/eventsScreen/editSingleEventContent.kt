package pt.ipp.estg.doctorbrain.screens.app

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.App
import pt.ipp.estg.doctorbrain.data.SingleEventViewModel
import pt.ipp.estg.doctorbrain.models.SingleEvent
import pt.ipp.estg.doctorbrain.models.enums.Local
import pt.ipp.estg.doctorbrain.screens.sytemInputs.DatePickerTunning
import pt.ipp.estg.doctorbrain.screens.sytemInputs.StartFinishTimePicker
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.LabelColors
import pt.ipp.estg.doctorbrain.ui.theme.getAllLabelColor
import pt.ipp.estg.doctorbrain.ui.theme.getLabelColorObjectByName

@SuppressLint("UnrememberedMutableState")
@Composable
fun EditSingleEventContent(singleEvent: SingleEvent, edited: () -> Unit) {
    val singleEventViewModel: SingleEventViewModel = viewModel()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    //input var
    var newsingleEvent by remember {
        mutableStateOf(singleEvent)
    }

    //Title TextField
    val str = remember { mutableStateOf(newsingleEvent.title) }
    val isNameValid by derivedStateOf {
        newsingleEvent.title.length >= 3
    }
    OutlinedTextField(
        value = str.value,
        placeholder = { Text(text = "Title") },
        label = { Text("Title") },
        onValueChange = {
            str.value = it
            newsingleEvent.title = it
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        isError = !isNameValid,
        colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
    )

    //Local Selection
    var expanded: Boolean by remember { mutableStateOf(false) }
    Row(modifier = Modifier
        .clickable { expanded = true }
        .fillMaxWidth()
        .height(40.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Local: ${newsingleEvent.local}")
        Icon(Icons.Default.ExpandMore, null)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            Local.values().forEach { item ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    newsingleEvent.local = item.name
                }) {
                    Text(text = item.name)
                }
            }
        }
    }

    //Event Hour Text
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = "Event Hour", style = InputLabel())
    }

    //Hour Selection
    StartFinishTimePicker(
        context = LocalContext.current,
        returnStartTime = { newsingleEvent.startHour = it },
        returnFinishTime = { newsingleEvent.finishtHour = it }
    )
    //Day Selection
    DatePickerTunning({ newsingleEvent.date = it }, newsingleEvent.date)


    //Colours Picker
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(text = "Color", style = InputLabel())
    }

    val selectedColor =
        remember { mutableStateOf<LabelColors?>(getLabelColorObjectByName(singleEvent.color)) }
    val colors = getAllLabelColor()
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(colors) { color ->
            Button(
                onClick = {
                    selectedColor.value = color
                    singleEvent.color = color.title
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
            coroutineScope.launch {
                singleEvent.cancelSchedule(App.ctx!!)
                newsingleEvent.schedule(App.ctx!!)
                singleEventViewModel.updateSingleEvent(newsingleEvent)
            }
            Toast.makeText(context, "Event Updated", Toast.LENGTH_SHORT).show()
            edited()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
    ) {
        Text(
            text = "Update", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold
        )
    }
}
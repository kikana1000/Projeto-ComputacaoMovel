package pt.ipp.estg.doctorbrain.screens.app

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.App
import pt.ipp.estg.doctorbrain.data.SingleEventViewModel
import pt.ipp.estg.doctorbrain.models.SingleEvent
import pt.ipp.estg.doctorbrain.models.enums.Local
import pt.ipp.estg.doctorbrain.screens.sytemInputs.DatePickerTunning
import pt.ipp.estg.doctorbrain.screens.sytemInputs.StartFinishTimePicker
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.LabelColors
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen
import pt.ipp.estg.doctorbrain.ui.theme.getAllLabelColor

/**
 * Bottom Sheet para adicionar Single Events
 * @param coroutineScope
 * @param bottomSheetScaffoldState
 * @param startHourI @param[startHourI] Hora default que começa o evento
 * @param finishHourI @param[finishHourI] Hora default que termina o evento
 */
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
fun AddEventBottomSheetContent(
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    startHourI: String,
    finishHourI: String
) {
    val singleEventViewModel: SingleEventViewModel = viewModel()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            //input var
            var title by remember { mutableStateOf("") }
            var expanded: Boolean by remember { mutableStateOf(false) }
            val local = remember { mutableStateOf(Local.Auditorio1) }
            val startHour = remember { mutableStateOf(startHourI) }
            val finishHour = remember { mutableStateOf(finishHourI) }
            val selectedColor = remember { mutableStateOf<LabelColors?>(null) }
            val dateSelected = remember{ mutableStateOf("") }


            //New Event
            Text(
                text = "New Event",
                style = TitleofScreen(),
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                },
                textAlign = TextAlign.Left
            )


            //Title TextField
            val isNameValid by derivedStateOf {
                title.length >= 3
            }
            OutlinedTextField(
                value = title,
                placeholder = { Text(text = "Title") },
                label = { Text("Title") },
                onValueChange = { title = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() }),
                isError = !isNameValid,
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
            )

            //Local Selection
            Row(modifier = Modifier
                .clickable { expanded = true }
                .fillMaxWidth()
                .height(40.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Local: ${local.value}")
                Icon(Icons.Default.ExpandMore, null)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Local.values().forEach { item ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            local.value = item
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
                returnStartTime = { startHour.value = it },
                returnFinishTime = { finishHour.value = it }
            )
            //Day Selection
            DatePickerTunning(){dateSelected.value = it}




            //Colours Picker
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(text = "Color", style = InputLabel())
            }
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
                        },
                        modifier = Modifier.size(50.dp),  //avoid the oval shape
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = color.color
                        )
                    ) {
                        if (color.equals(selectedColor.value)) {
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
                    if (validateEvent(
                            startHour.value,
                            finishHour.value,
                            selectedColor.value?.title,
                            title,
                            local.value.toString()
                        )
                    ) {
                        val singleEvent=SingleEvent(
                            startHour.value,
                            finishHour.value,
                            dateSelected.value,
                            selectedColor.value!!.title,
                            title,
                            local.value.toString()
                        )
                        singleEvent.schedule(App.ctx!!)

                        singleEventViewModel.addSingleEvent(
                            singleEvent
                        )
                        title = ""
                        local.value = Local.Auditorio1
                        selectedColor.value = LabelColors.green
                        coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }

                    } else {
                        Toast.makeText(context, "Empty fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            ) {
                Text(
                    text = "Create", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun validateEvent(
    startTime: String,
    finishtTime: String,
    selectedColor: String?,
    title: String,
    local: String,
): Boolean {
    if (startTime != ""
        && finishtTime != ""
        && selectedColor != ""
        && title != ""
        && local != ""
    ) {
        return true
    }
    return false
}
package pt.ipp.estg.doctorbrain.screens.app

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
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
import pt.ipp.estg.doctorbrain.data.SchoolClassViewModel
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.models.enums.Local
import pt.ipp.estg.doctorbrain.models.enums.WeekDays
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.GetListofHourDate
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.LabelColors
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen
import pt.ipp.estg.doctorbrain.ui.theme.getAllLabelColor
import java.time.LocalTime

/**
 * Bottom Sheet para criar uma nova SchoolClass
 *
 * @param coroutineScope
 * @param bottomSheetScaffoldState
 */
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
fun AddClassBottomSheetContent(context: Context,
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    startHourI: String,
    finishHourI: String
) {
    val schoolClassViewModel: SchoolClassViewModel = viewModel()
    val focusManager = LocalFocusManager.current
    var schools=schoolClassViewModel.getAllSchoolClass().observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
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
            val selectedColor = remember { mutableStateOf<LabelColors?>(null) }
            val countofHours = remember { mutableStateOf(0) }
            val startHour = remember { mutableStateOf(startHourI) }
            val finishHour = remember { mutableStateOf(finishHourI) }
            val emptyHourDate = HourDateSchoolClass(
                0,
                startHour.value,
                finishHour.value,
                WeekDays.MONDAY.name,
                true
            )
            val list: List<HourDateSchoolClass> = listOf(emptyHourDate)
            val listofHourDate = remember { mutableStateOf(list) }


            //New Class
            Text(
                text = "New Class",
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


            //Local PickUp Options
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

            //Class Hour Text
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(text = "Class Hour", style = InputLabel())
            }

            //Get Hour and Day
            GetListofHourDate(context, {
                listofHourDate.value = it
                Log.d("LISTT", listofHourDate.value.toString())
            }, { countofHours.value = it })


            //Colour Pick Up
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

            //Create Button
            Button(
                onClick = {
                    if (title.length > 2 && selectedColor.value != null) {

                        schoolClassViewModel.addSchoolClass(
                            SchoolClass(
                                selectedColor.value!!.title,
                                title,
                                local.value.toString()
                            ), listofHourDate.value, countofHours.value
                        )
                        for(a in 0..countofHours.value){
                            listofHourDate.value.get(a).schedule(App.ctx!!,title)
                        }
                        Toast.makeText(context, "New School Class", Toast.LENGTH_SHORT).show()
                        //Reset Values
                        title = ""
                        expanded = false
                        local.value = Local.Auditorio1
                        selectedColor.value = null
                        countofHours.value = 0
                        listofHourDate.value = list
                        startHour.value = "${LocalTime.now().hour}:${LocalTime.now().minute}"
                        finishHour.value =
                            "${LocalTime.now().plusHours(1).hour}:${LocalTime.now().minute}"
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    } else {
                        Toast.makeText(context, "Empty fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                elevation = ButtonDefaults.elevation(defaultElevation = 10.dp)
            ) {
                Text(
                    text = "Create", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
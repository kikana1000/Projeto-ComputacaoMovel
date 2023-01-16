package pt.ipp.estg.doctorbrain.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pt.ipp.estg.doctorbrain.R
import pt.ipp.estg.doctorbrain.data.SchoolClassViewModel
import pt.ipp.estg.doctorbrain.data.SingleEventViewModel
import pt.ipp.estg.doctorbrain.screens.app.eventsScreen.InfoSingleEventBottomSheetContent
import pt.ipp.estg.doctorbrain.graphs.Graph
import pt.ipp.estg.doctorbrain.models.IpmaObject
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.models.SingleEvent
import pt.ipp.estg.doctorbrain.models.WeatherTypeObject
import pt.ipp.estg.doctorbrain.retrofitFun.IPMA_ViewModel
import pt.ipp.estg.doctorbrain.screens.app.AddClassBottomSheetContent
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.InfoClassBottomSheetContent
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.CalendarTunning
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.Info.ShowClassAndEventsSort
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.Weather.PrintWeatherData
import pt.ipp.estg.doctorbrain.ui.theme.CardTitle
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen
import java.time.LocalDate
import java.time.LocalTime


/**
 * Screen do Calendario que demonstra as SchoolClasses
 * @param navController @param[navController] Faz a navegação para a SchoolClassScreen()
 * */
@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun CalendarScreen(navController: NavController, context: Context) {
    //Class Room Stuff
    val schoolClassViewModel: SchoolClassViewModel = viewModel()
    val classes = schoolClassViewModel.getAllSchoolClass().observeAsState()
    val hours = schoolClassViewModel.getAllHourDate().observeAsState()

    //SingleEvent Room Stuff
    val singleEventViewModel: SingleEventViewModel = viewModel()
    val singleEvents = singleEventViewModel.getAll().observeAsState()

    //Ipma Stuff
    val ipmaViewmodel: IPMA_ViewModel = viewModel()
    val weatherObject: IpmaObject? = ipmaViewmodel.getMyWeather()?.observeAsState()?.value
    val weatherTypeObject: WeatherTypeObject? =
        ipmaViewmodel.getMyWeatherType()?.observeAsState()?.value

    //Selected Date
    val date = remember { mutableStateOf(LocalDate.now()) }

    //Selected SchoolClassCard
    val showClass = remember {
        mutableStateOf(SchoolClass("", "", ""))
    }
    //Selected SingleEventCard
    val showSingleEvent = remember {
        mutableStateOf(SingleEvent("", "", "", "", "", ""))
    }


    //infoSingleEventBottomSheet
    val infoSingleEventBottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val infoSingleEventCoroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = infoSingleEventBottomSheetScaffoldState, sheetContent = {
            InfoSingleEventBottomSheetContent(
                coroutineScope = infoSingleEventCoroutineScope,
                bottomSheetScaffoldState = infoSingleEventBottomSheetScaffoldState,
                singleEvent = showSingleEvent.value
            ) { navController.navigate(Graph.CALENDAR) }
        }, sheetPeekHeight = 0.dp
    ) {
        //infoClassBottomSheet
        val infoSchoolClassBottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
        )
        val infoSchoolClassCoroutineScope = rememberCoroutineScope()
        BottomSheetScaffold(
            scaffoldState = infoSchoolClassBottomSheetScaffoldState, sheetContent = {
                InfoClassBottomSheetContent(
                    context = context,
                    coroutineScope = infoSchoolClassCoroutineScope,
                    bottomSheetScaffoldState = infoSchoolClassBottomSheetScaffoldState,
                    schoolClass = showClass.value
                ) { navController.navigate(Graph.CALENDAR) }
            }, sheetPeekHeight = 0.dp
        ) {
            //addClassBottomSheet
            val addSchoolClassBottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
            )
            val addSchoolClassCoroutineScope = rememberCoroutineScope()
            BottomSheetScaffold(
                scaffoldState = addSchoolClassBottomSheetScaffoldState, sheetContent = {
                    AddClassBottomSheetContent(
                        context,
                        addSchoolClassCoroutineScope,
                        addSchoolClassBottomSheetScaffoldState,
                        "${LocalTime.now().hour}:${LocalTime.now().minute}",
                        "${LocalTime.now().plusHours(1).hour}:${LocalTime.now().minute}"
                    )
                }, sheetPeekHeight = 0.dp
            ) {
                CalendarTunning {
                    date.value = it
                }
                Column(modifier = Modifier.padding(10.dp)) {
                    PrintWeatherData(weatherObject, weatherTypeObject)

                    //Next Class Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        //Next Class Title
                        Text(text = "Events Today", style = TitleofScreen())

                        Row() {
                            //ClassList Button
                            IconButton(onClick = {
                                navController.navigate(Graph.CLASSES)
                            }) {
                                Icon(Icons.Default.School, contentDescription = "Show Classes")
                            }
                            //ClassAdd Button
                            IconButton(onClick = {

                                changeBottomSheetState(
                                    addSchoolClassCoroutineScope,
                                    addSchoolClassBottomSheetScaffoldState
                                )
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Class")
                            }
                        }
                    }
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        ShowClassAndEventsSort(hours.value,
                            classes.value,
                            singleEvents.value,
                            date.value,
                            {
                                showClass.value = it
                                changeBottomSheetState(
                                    infoSchoolClassCoroutineScope,
                                    infoSchoolClassBottomSheetScaffoldState
                                )
                            },
                            {
                                showSingleEvent.value = it
                                changeBottomSheetState(
                                    infoSingleEventCoroutineScope,
                                    infoSingleEventBottomSheetScaffoldState
                                )
                            })
                    }

                }
            }
        }
    }
}
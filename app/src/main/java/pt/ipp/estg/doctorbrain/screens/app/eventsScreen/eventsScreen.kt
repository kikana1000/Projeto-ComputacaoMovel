package pt.ipp.estg.doctorbrain.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.data.SingleEventViewModel
import pt.ipp.estg.doctorbrain.models.SingleEvent
import pt.ipp.estg.doctorbrain.screens.app.AddEventBottomSheetContent
import pt.ipp.estg.doctorbrain.screens.app.eventsScreen.InfoSingleEventBottomSheetContent
import pt.ipp.estg.doctorbrain.screens.app.eventsScreen.SingleEventCard
import pt.ipp.estg.doctorbrain.graphs.Graph
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen
import java.time.LocalTime

/**
 * Screen que demonstra todos os eventos existentes e que permite editar e visualizar
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EventsScreen(navController: NavController) {
    val bottomSheetScaffoldStateInfo = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val sEvent: MutableState<SingleEvent> = remember {
        mutableStateOf(SingleEvent("", "", "", "", "", ""))
    }


    val bottomSheetScaffoldStateAdd = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldStateAdd, sheetContent = {
            AddEventBottomSheetContent(
                coroutineScope = coroutineScope,
                bottomSheetScaffoldState = bottomSheetScaffoldStateAdd,
                "${LocalTime.now().hour}:${LocalTime.now().minute}",
                "${LocalTime.now().plusHours(1).hour}:${LocalTime.now().minute}"
            )
        }, sheetPeekHeight = 0.dp
    ) {
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldStateInfo, sheetContent = {
                InfoSingleEventBottomSheetContent(
                    coroutineScope = coroutineScope,
                    bottomSheetScaffoldState = bottomSheetScaffoldStateInfo,
                    singleEvent = sEvent.value
                ){
                    navController.navigate(Graph.EVENTS)
                }
            }, sheetPeekHeight = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //Title
                    Text(text = "Events", style = TitleofScreen())
                    //addEventButton
                    IconButton(onClick = {
                        coroutineScope.launch {
                            if (bottomSheetScaffoldStateAdd.bottomSheetState.isCollapsed) {
                                bottomSheetScaffoldStateAdd.bottomSheetState.expand()
                            } else {
                                bottomSheetScaffoldStateAdd.bottomSheetState.collapse()
                            }
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Event")
                    }
                }
                //SearchBar
                SearchViewEvents(moreinfo = {
                    sEvent.value = it
                    coroutineScope.launch {
                        bottomSheetScaffoldStateInfo.bottomSheetState.expand()
                    }
                })
            }
        }
    }
}


@Composable
fun SearchViewEvents(moreinfo: (SingleEvent) -> Unit) {
    val state = remember { mutableStateOf(TextFieldValue("")) }
    var focusManager = LocalFocusManager.current
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth(),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() })

    )
    Column(modifier = Modifier
        .padding(top = 10.dp)
        .verticalScroll(rememberScrollState())) {
        SearchEvents(search = state.value.text, moreinfo)
    }
}


@Composable
fun SearchEvents(search: String, moreinfo: (SingleEvent) -> Unit) {
    val singleEventViewModel: SingleEventViewModel = viewModel()
    var classes = singleEventViewModel.getAll().observeAsState()
    if (search != "") {
        classes = singleEventViewModel.search(search).observeAsState()
    }
    classes.value?.forEach {
        SingleEventCard(sEvent = it, moreinfo,false)
    }
}
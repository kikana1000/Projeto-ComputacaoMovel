package pt.ipp.estg.doctorbrain.screens.app.calendarScreen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.data.SchoolClassViewModel
import pt.ipp.estg.doctorbrain.graphs.Graph
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.InfoClassBottomSheetContent
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.SearchViewSchoolClasses
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen

/**
* UI da Main Page
* @param navController @param[navController] Faz a navegação para a
* */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SchoolClassScreen(navController: NavController,context: Context) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val sclass: MutableState<SchoolClass> = remember {
        mutableStateOf(SchoolClass("","",""))
    }
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState, sheetContent = {
            InfoClassBottomSheetContent(context = context,
                coroutineScope = coroutineScope,
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                schoolClass = sclass.value
            ) { navController.navigate(Graph.CLASSES) }
        }, sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "School Classes", style = TitleofScreen())
                IconButton(onClick = { navController.navigate(Graph.CALENDAR) }) {
                    Icon(Icons.Default.Close, "go to CalendarScreen")
                }
            }
            SearchViewSchoolClasses(moreinfo = {
                sclass.value = it
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            }
            )
        }
    }
}

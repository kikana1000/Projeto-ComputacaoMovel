package pt.ipp.estg.doctorbrain.screens.app.calendarScreen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.App
import pt.ipp.estg.doctorbrain.data.SchoolClassViewModel
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.screens.sytemInputs.SimpleAlertDialog
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.Info.InfoContent
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.edit.EditSchoolClassContent
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen

/**
 * Bottom Sheet que apresenta informações da SchoolClass selecionada
 * @param coroutineScope @param[coroutineScope]
 * @param bottomSheetScaffoldState @param[bottomSheetScaffoldState] Scaffold de fundo onde irá apresentar a informação
 * @param schoolClass @param[schoolClass] SchoolClass selecionada
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfoClassBottomSheetContent(context: Context,
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    schoolClass: SchoolClass?,
    refresh: () -> Unit
) {
    val schoolClassViewModel: SchoolClassViewModel = viewModel()
    val list  =schoolClassViewModel.getHourDateBySchooClass(schoolClass!!._eventID).observeAsState()
    Log.d("List Not", list.value.toString())
    val edit = remember { mutableStateOf(false) }
    val show = remember { mutableStateOf(false) }
    SimpleAlertDialog(
        show = show.value,
        onConfirm = {
            if (schoolClass != null) {

                for (a in list.value!!){
                    a.cancelSchedule(App.ctx!!)
                }
                schoolClassViewModel.deleteSchoolClass(schoolClass)
            }
            coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
            show.value = false
        },
        onDismiss = { show.value = false },
        textTittle = "Delete SchoolClass?"
    )

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (schoolClass != null) {
                    Text(
                        text = schoolClass.title,
                        style = TitleofScreen(),
                        color = Color.Black,
                    )
                }

                Row() {
                    IconButton(onClick = {
                        edit.value = !edit.value
                    }) {
                        Icon(Icons.Default.Edit, "Delete Class")
                    }
                    if (!edit.value) {
                        IconButton(onClick = {
                            show.value = true
                        }) {
                            Icon(Icons.Default.Delete, "Delete Class")
                        }
                    }
                }
            }

            if (edit.value) {
                if (schoolClass != null) {
                    EditSchoolClassContent(
                        schoolClass
                    ) { refresh() }
                }
            } else {
                if (schoolClass != null) {
                    InfoContent(schoolClass)
                }
            }
        }
    }
}
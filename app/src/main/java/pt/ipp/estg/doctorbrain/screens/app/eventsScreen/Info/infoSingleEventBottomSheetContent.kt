package pt.ipp.estg.doctorbrain.screens.app.eventsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
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
import pt.ipp.estg.doctorbrain.data.SingleEventViewModel
import pt.ipp.estg.doctorbrain.models.SingleEvent
import pt.ipp.estg.doctorbrain.screens.app.EditSingleEventContent
import pt.ipp.estg.doctorbrain.screens.app.InfoSingleEventContent
import pt.ipp.estg.doctorbrain.screens.sytemInputs.SimpleAlertDialog
import pt.ipp.estg.doctorbrain.screens.sytemInputs.dateParse
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen
import java.time.LocalDate

/**
 * Bottom Sheet que apresenta informações do Event selecionado
 * @param coroutineScope @param[coroutineScope]
 * @param bottomSheetScaffoldState @param[bottomSheetScaffoldState] Scaffold de fundo onde irá apresentar a informação
 * @param singleEvent @param[singleEvent] singleEvent selecionado
 */
@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfoSingleEventBottomSheetContent(
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    singleEvent: SingleEvent,
    updateViewModel: () -> Unit
) {
    val schoolClassViewModel: SingleEventViewModel = viewModel()


    val edit = remember { mutableStateOf(false) }
    val show = remember { mutableStateOf(false) }
    SimpleAlertDialog(show = show.value, onConfirm = {
        singleEvent.cancelSchedule(App.ctx!!)
        schoolClassViewModel.deleteSingleEvent(singleEvent)
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
        show.value = false
    }, onDismiss = { show.value = false }, textTittle = "Delete Event?"
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //Title
                Text(
                    text = singleEvent.title,
                    style = TitleofScreen(),
                    color = Color.Black,
                )
                Row() {
                    if (singleEvent.date != "") {
                        if (LocalDate.now() < dateParse(singleEvent.date)) {
                            IconButton(onClick = {
                                edit.value = !edit.value
                            }) {
                                Icon(Icons.Default.Edit, "Edit Event")
                            }
                        }
                    }
                    if (!edit.value) {
                        IconButton(onClick = {
                            show.value = true
                        }) {
                            Icon(Icons.Default.Delete, "Delete Event")

                        }
                    }
                }
            }
            if (edit.value) EditSingleEventContent(singleEvent) {
                edit.value = false
                updateViewModel()
            }
            else InfoSingleEventContent(singleEvent)
        }
    }
}



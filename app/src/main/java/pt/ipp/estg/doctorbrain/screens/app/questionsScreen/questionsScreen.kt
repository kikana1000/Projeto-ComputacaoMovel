package pt.ipp.estg.doctorbrain.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.firestoreFun.FireBaseViewModel
import pt.ipp.estg.doctorbrain.screens.app.AddQuestionBottomSheetContent
import pt.ipp.estg.doctorbrain.screens.app.questionsScreen.ListAndSearchQuestions
import pt.ipp.estg.doctorbrain.ui.theme.*

/**
 * Screen que demonstra todos as questions existentes a que o utilizador tem acesso e permite editar as mesmas
 * @param onSelectedQuestion
 * @param onGroupList
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun QuestionsScreen(onGroupsList: () -> Unit, onSelectedQuestion: (String) -> Unit) {
    val fireBaseViewModel: FireBaseViewModel = viewModel()
    fireBaseViewModel.getQuestions()
    //Stuff
    val bottomSheetScaffoldStateAdd = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    val questionsList = fireBaseViewModel.questionsList.observeAsState().value

    BottomSheetScaffold( //bottomSheetAdd
        scaffoldState = bottomSheetScaffoldStateAdd, sheetContent = {
            AddQuestionBottomSheetContent(
                coroutineScope = coroutineScope,
                bottomSheetScaffoldState = bottomSheetScaffoldStateAdd,
                onReload = { fireBaseViewModel.getQuestions() }
            )
        }, sheetPeekHeight = 0.dp
    ) {

        //Header
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
                Text(text = "Questions", style = TitleofScreen())

                Row() {
                    IconButton(onClick = { onGroupsList() }) {
                        Icon(Icons.Default.Groups, contentDescription = "Groups")
                    }
                    IconButton(onClick = {
                        changeBottomSheetState(coroutineScope, bottomSheetScaffoldStateAdd)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Question")
                    }
                }
            }
            ListAndSearchQuestions(questionsList) {
                onSelectedQuestion(it)
            }
        }
    }
}


//muda o estado da bottomSheet
@OptIn(ExperimentalMaterialApi::class)
fun changeBottomSheetState(
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    coroutineScope.launch {
        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
            bottomSheetScaffoldState.bottomSheetState.expand()
        } else {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }
}

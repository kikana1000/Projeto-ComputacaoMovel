package pt.ipp.estg.doctorbrain.screens.app.questionsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ToggleOff
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import pt.ipp.estg.doctorbrain.firestoreFun.FireBaseViewModel
import pt.ipp.estg.doctorbrain.models.*
import pt.ipp.estg.doctorbrain.ui.theme.*
import pt.ipp.estg.doctorbrain.models.enums.State.Open

/**
 * Bottom Sheet que apresenta informações do Event selecionado
 *
 * @param coroutineScope @param[coroutineScope]
 * @param bottomSheetScaffoldState @param[bottomSheetScaffoldState] Scaffold de fundo onde irá apresentar a informação
 * @param schoolClass @param[schoolClass] SchoolClass selecionada
 */
@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Composable
fun InfoQuestionScreen(
    selectedQuestionID: String,
    goBack: String,
    navController: NavController
) {

    val fireBaseViewModel: FireBaseViewModel = viewModel()
    fireBaseViewModel.getQuestionById(selectedQuestionID) //tentar tirar isto daqui ... faz multiplas chamadas a BD

    val selectedQuestion = fireBaseViewModel.selectedQuestion.observeAsState().value

    //BottomBar
    Scaffold(
        bottomBar = {
            //Answer TextField
            var answer by remember { mutableStateOf("") }
            val isAnswerValid by derivedStateOf {
                answer.length >= 3
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(primary),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    //TextField para escrever Answer
                    TextField(
                        enabled = if (selectedQuestion == null) false else selectedQuestion.State == Open,
                        value = answer,
                        onValueChange = { answer = it },
                        placeholder = { Text("Reply...") },
                        singleLine = false,
                        colors =
                        if (isSystemInDarkTheme()) {
                            TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        } else {
                            TextFieldDefaults.textFieldColors(
                                backgroundColor = if (selectedQuestion == null) Color.Gray else if (selectedQuestion.State == Open) Color.White else Color.Gray,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        //Buttao de SendMensage
                        trailingIcon = {
                            if (isAnswerValid) {
                                IconButton(onClick = {
                                    fireBaseViewModel.addAnswerToQuestion(Answer(Description = answer))
                                    answer = ""
                                }) {
                                    Icon(
                                        Icons.Default.Send,
                                        "Open/Close Question",
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    )
    //Content of the Page
    { innerPadding ->
        // Apply the padding globally to the whole BottomNavScreensController
        Box(modifier = Modifier.padding(innerPadding)) {
            // Screen content
            InfoQuestion(selectedQuestion) {
                navController.navigate(goBack)
            }
        }

    }
}


/**
 * Composable com a info da selectedQuestion
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun InfoQuestion(question: Question?, goBack: () -> Unit) {
    if (question == null) return

    val fireBaseViewModel: FireBaseViewModel = viewModel()

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //Titulo da Question
                Column() {
                    IconButton(onClick = { goBack() }) {
                        Icon(Icons.Default.ArrowBack, "goBack")
                    }
                    Text(
                        text = question.Title,
                        style = TitleofScreen(),
                    )
                }


                //So o owner da question pode modificar estes campos
                val currentUserIsTheOwner = fireBaseViewModel.isCurrentUserTheQuestionOwner()
                Row(Modifier.padding(5.dp)) {
                    Column(Modifier.padding(end = 5.dp)) {
                        //IsPrivate CheckBox
                        val isChecked = derivedStateOf { question.IsPutlic }
                        if (currentUserIsTheOwner) {
                            IconButton(onClick = {
                                fireBaseViewModel.changeIsPrivateQuestion()
                            }) {
                                Icon(
                                    imageVector = if (isChecked.value) Icons.Outlined.ToggleOn else Icons.Outlined.ToggleOff,
                                    "Open/Close Question",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Text(text = if (isChecked.value) "Public" else "Private")
                    }

                    Column() {
                        //Icon de Abrir/Fechar Question
                        val isChecked = derivedStateOf { question.State == Open }
                        if (currentUserIsTheOwner) {
                            IconButton(onClick = {
                                fireBaseViewModel.changeQuestionState()
                            }) {
                                Icon(
                                    imageVector = if (isChecked.value) Icons.Outlined.ToggleOn else Icons.Outlined.ToggleOff,
                                    "Open/Close Question",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Text(text = if (isChecked.value) "Open" else "Closed")
                    }
                }
            }
            val answers = question.Answers
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            LazyColumn(
                state = listState,
            ) {
                coroutineScope.launch {
                    listState.scrollToItem(answers.size+1)
                }
                item() {
                    //Description
                    Card(
                        modifier = cardStyleModifier(),
                        backgroundColor = primaryVariant,
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {

                            //Get UserName from FireBase
                            val user: User? = fireBaseViewModel.getUserProfileById(question.OwnerID)
                                .observeAsState().value
                            Text(
                                text = "@" + if (user != null) user.name else "",
                                style = InputLabel(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Left,
                                color = Color.Black
                            )
                            Text(
                                text = question.Description,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Left,
                                color = Color.Black
                            )
                        }
                    }
                }
                //da print das answers
                if (answers.isNotEmpty()) for (answer in answers) {
                    item() {
                        InfoAnswer(answer = answer)
                    }
                }
            }
        }
    }
}


/**
 * Da print da answer recebida de parametro
 */
@Composable
fun InfoAnswer(answer: Answer) {
    val auth = Firebase.auth.currentUser!!.uid
    val fireBaseViewModel: FireBaseViewModel = viewModel()
    Card(
        modifier = cardStyleModifier(),
        backgroundColor = if (answer._ownerID == auth) {
            primaryVariant
        } else {
            primary
        },
        contentColor = if (answer._ownerID == auth) {
            Color.Black
        } else {
            Color.White
        }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {

            //Get UserName from FireBase
            val user: User? = fireBaseViewModel.getUserProfileById(answer._ownerID)
                .observeAsState().value
            Text(
                text = "@" + if (user != null) user.name else "",
                style = InputLabel(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
            )
            Text(
                text = answer.Description,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
            )
        }
    }
}



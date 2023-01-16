package pt.ipp.estg.doctorbrain.screens.app.questionsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.messaging.FirebaseMessaging
import pt.ipp.estg.doctorbrain.models.Question
import pt.ipp.estg.doctorbrain.models.enums.State

/**
 * Lista as Questions enviadas
 * Sort pelo Question.State (Open/Closed)
 * Search por string em Question.Tile e Question.Description
 */
@Composable
fun ListAndSearchQuestions(questionsList: List<Question>?, onSelectedQuestion: (String) -> Unit) {
    var search by remember { mutableStateOf("") }
    SearchBar { search = it }
    val state = remember { mutableStateOf(State.Open) }
    // Select State Tab
    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Open", "Close")
    Column(Modifier.padding(bottom = 5.dp)) {
        TabRow(selectedTabIndex = tabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(text = title) })
            }
        }
        when (tabIndex) { // 6.
            0 -> state.value = State.Open
            1 -> state.value = State.Closed
        }
    }
    //List Questions baseado no State selecionado anteriormente
    if (questionsList != null) {

        //Search by Question.Title and Question.Description
        if (search != "") {
            val searchedList = remember { mutableListOf<Question>() }
            searchedList.removeAll(searchedList)
            questionsList.forEach {
                if (it.Title.contains(search) || it.Description.contains(search)) {
                    searchedList.add(it)
                }
            }.also {
                ListQuestions(
                    searchedList, moreInfo = { selectedQuestion ->
                        onSelectedQuestion(selectedQuestion.QuestionID)
                    }, state.value
                )
            }
        } else {
            ListQuestions(
                questionsList,
                moreInfo = { selectedQuestion ->
                    onSelectedQuestion(selectedQuestion.QuestionID)
                }, state.value
            )
        }
    }
}
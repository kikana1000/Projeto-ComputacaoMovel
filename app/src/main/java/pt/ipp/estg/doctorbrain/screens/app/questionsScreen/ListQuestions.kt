package pt.ipp.estg.doctorbrain.screens.app.questionsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipp.estg.doctorbrain.models.Question
import pt.ipp.estg.doctorbrain.models.enums.State
import pt.ipp.estg.doctorbrain.ui.theme.CardTitle
import pt.ipp.estg.doctorbrain.ui.theme.cardStyleModifier
import pt.ipp.estg.doctorbrain.ui.theme.primary


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListQuestions(questionsList: List<Question>, moreInfo: (Question) -> Unit,state:State) {
    if (questionsList.isNotEmpty()) {
        LazyColumn(state = rememberLazyListState()) {
            items(questionsList) { question ->
                if (question.State == state) {
                    Card(
                        modifier = cardStyleModifier(),
                        backgroundColor = primary,
                        onClick = {
                            moreInfo(question)
                        }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = question.Title, color = Color.White, style = CardTitle())
                            if (question.Answers.isNotEmpty()) {
                                Text(text = question.Answers.last().Description)
                            }
                            Text(text = question.State.toString(), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
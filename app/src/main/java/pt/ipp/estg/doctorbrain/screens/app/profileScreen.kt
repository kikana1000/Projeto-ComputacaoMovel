package pt.ipp.estg.doctorbrain.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import pt.ipp.estg.doctorbrain.firestoreFun.FireBaseViewModel
import pt.ipp.estg.doctorbrain.models.User
import pt.ipp.estg.doctorbrain.models.enums.State
import pt.ipp.estg.doctorbrain.screens.app.questionsScreen.ListQuestions
import pt.ipp.estg.doctorbrain.ui.theme.CardTitle
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen
import pt.ipp.estg.doctorbrain.ui.theme.cardStyleBorder
import pt.ipp.estg.doctorbrain.ui.theme.cardStyleModifier

/**
 * Screen que demonstra o perfil do utilizador e as questions feita pelo o mesmo
 * @param onLogout @[onLogout] Unit to call logout Function
 */
@Composable
fun ProfileScreen(onLogout: () -> Unit, onSelectedQuestion: (String) -> Unit) {
    //devia ser outra coisa q não list ... mas paciencia ...
    val fireBaseViewModel: FireBaseViewModel = viewModel()
    val user = fireBaseViewModel.userProfile.observeAsState().value
    fireBaseViewModel.getQuestions(thatImTheOwnerOf = true)
    val questionsList = fireBaseViewModel.questionsList.observeAsState().value



    Column(modifier = Modifier
        .padding(10.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.End
        ) {
            //Icon de Logout
            IconButton(onClick = {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("user_${Firebase.auth!!.uid!!}")
                fireBaseViewModel.auth.signOut()
                onLogout()
            }) {
                Icon(
                    Icons.Default.Logout,
                    "Logout User",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.AccountCircle, "",
                Modifier.size(128.dp)
            )
        }
        Text(text = "Informação", style = TitleofScreen())
        userProfile(user)

        Text(text = "Arquivos", style = TitleofScreen())
        //listofQuestions(questions = getExemples())

        //lista as questions da list das quais sou owner
        if (questionsList != null) {
            ListQuestions(questionsList,
                moreInfo = { selectedQuestion ->
                    onSelectedQuestion(selectedQuestion.QuestionID)
                }, State.Closed)
        }
    }
}

@Composable
fun userProfile(user: User?) {
    if (user != null) {
        Card(modifier = cardStyleModifier(), border = cardStyleBorder()) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "Username: ${user.name}", style = CardTitle())
                Text(text = "Telemovel: ${user.contacto}")
                Text(text = "Numero de Aluno: ${user.student_number}")
                Text(text = "Email: ${user.email}")
            }
        }
    }
}
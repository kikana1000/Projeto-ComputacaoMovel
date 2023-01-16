package pt.ipp.estg.doctorbrain.screens.app

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.data.GroupViewModel
import pt.ipp.estg.doctorbrain.models.Contact
import pt.ipp.estg.doctorbrain.models.Group
import pt.ipp.estg.doctorbrain.screens.sytemInputs.SelectGroupMembers
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen

/**
 * Conteudo a aparecer no Bottom Sheet da funcionalidade Adicionar Screen
 */
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
fun AddGroupBottomSheetContent(
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
) {
    val context = LocalContext.current
    val groupViewModel: GroupViewModel = viewModel()
    val focusManager = LocalFocusManager.current
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
            //input var
            var title by remember { mutableStateOf("") }
            //New Group
            Text(
                text = "New Group",
                style = TitleofScreen(),
                color = Color.Black,
                textAlign = TextAlign.Left
            )


            //Title TextField
            val isNameValid by derivedStateOf {
                title.length >= 3
            }
            OutlinedTextField(
                value = title,
                placeholder = { Text(text = "Title") },
                label = { Text("Title") },
                onValueChange = { title = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() }),
                isError = !isNameValid,
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
            )

            //Select Participants
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(text = "Participants", style = InputLabel())
            }

            val contacts = remember{ mutableListOf<Contact>() }


            SelectGroupMembers(contacts) {
                contacts.removeAll(contacts)
                contacts.addAll(it)
            }

            Button(
                onClick = {
                    if (validateGroup(title, contacts)) {
                        groupViewModel.insert(Group(title), contacts)
                        coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                    } else {
                        Toast.makeText(context, "Empty fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            ) {
                Text(
                    text = "Create", modifier = Modifier.padding(5.dp), fontWeight = FontWeight.Bold
                )
            }
            if(bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                title = ""
                contacts.removeAll(contacts)
            }
        }
    }
}

fun validateGroup(
    title: String,
    contacts: List<Contact>
): Boolean {
    if (title != "" &&
        contacts.isNotEmpty()
    ) {
        return true
    }
    return false
}
package pt.ipp.estg.doctorbrain.screens.app

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.data.GroupViewModel
import pt.ipp.estg.doctorbrain.firestoreFun.FireBaseViewModel
import pt.ipp.estg.doctorbrain.models.*
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.LabelColors
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
fun AddQuestionBottomSheetContent(
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onReload: () -> Unit
) {
    val fireBaseViewModel: FireBaseViewModel = viewModel()
    val groupViewModel: GroupViewModel = viewModel()
    val groups = groupViewModel.getallGroups()?.observeAsState()
    val phoneNumbersInGroup = groupViewModel.getallPhoneNumber()?.observeAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val allUsersList = fireBaseViewModel.getUsers().observeAsState().value

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
            var description by remember { mutableStateOf("") }
            var expanded: Boolean by remember { mutableStateOf(false) }
            val selectedColor = remember { mutableStateOf<LabelColors?>(LabelColors.green) } //co


            //New Question
            Text(
                text = "New Question",
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
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = !isNameValid,
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
            )


            //Description TextField
            val isDescriptionValid by derivedStateOf {
                description.length >= 3
            }
            OutlinedTextField(
                value = description,
                placeholder = { Text(text = "Description") },
                label = { Text("Description") },
                onValueChange = { description = it },
                singleLine = false,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() }),
                isError = !isDescriptionValid,
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
            )


            //Select Groups
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(text = "Groups", style = InputLabel())
            }

            //Single Contacts
            val contacts = remember { mutableListOf<Contact>() }
            val groupContacts = remember { mutableListOf<Contact>() }
            val questionParticipantsIDList = remember { mutableListOf<String>() }

            var selectedGroup by remember { mutableStateOf(Group("None")) }
            //Pick a group
            if (allUsersList != null) {
                if (groups?.value != null) {
                    Row(modifier = Modifier
                        .clickable { expanded = true }
                        .fillMaxWidth()
                        .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Group: ${selectedGroup.Title}")
                        Icon(Icons.Default.ExpandMore, null)
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DropdownMenuItem(onClick = {
                                expanded = false
                                contacts.clear()
                                groupContacts.clear()
                                questionParticipantsIDList.clear()
                            }) {
                                Text(text = "None")
                            }
                            groups.value!!.forEach { item ->
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    updateGroup(item._groupID, phoneNumbersInGroup!!.value) {
                                        contacts.clear()
                                        contacts.addAll(it)
                                        groupContacts.clear()
                                        groupContacts.addAll(it)
                                        contactsToUser(allUsersList, contacts) { userstoAdd ->
                                            updateQuestionParticipantsIDList(userstoAdd) { list ->
                                                questionParticipantsIDList.clear()
                                                questionParticipantsIDList.addAll(list)
                                            }
                                        }

                                        Log.d("contacts", contacts.toString())
                                        Log.d(
                                            "questionParticipantsIDList",
                                            questionParticipantsIDList.toString()
                                        )

                                    }
                                    selectedGroup = item
                                }) {
                                    Text(text = item.Title)
                                }
                            }
                        }
                    }
                }
            }

            //Create Question Button
            Button(
                onClick = {
                    if (validateQuestion(
                            selectedColor.value!!.title,
                            title, description, questionParticipantsIDList
                        )
                    ) {
                        Log.d("questionParticipantsIDList", questionParticipantsIDList.toString())
                        fireBaseViewModel.addQuestion(

                            Question(
                                Title = title,
                                Description = description,
                                Participants = questionParticipantsIDList
                            )
                        )

                        onReload() //mudando o questionslist para liveData, deixa de ser preciso dar reload

                        title = ""
                        description = ""
                        selectedColor.value = LabelColors.green
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
        }
    }
}

fun updateQuestionParticipantsIDList(
    returnedAddedUsers: MutableList<User>?,
    returnQuestionParticipantsIDList: (MutableList<String>) -> Unit
) {
    val questionParticipantsIDList = mutableListOf<String>()
    if (returnedAddedUsers != null) for (addedUser in returnedAddedUsers) questionParticipantsIDList.add(
        addedUser.id
    ).also { returnQuestionParticipantsIDList(questionParticipantsIDList) }
}

fun updateGroup(
    _groupID: Int,
    list: List<PhoneNumber>?,
    returnList: (List<Contact>) -> Unit
) {
    val newList = mutableListOf<Contact>()
    list?.forEach {
        if (it.group == _groupID) {
            newList.add(Contact(it.name, it.phoneNumber))
        }
    }.also {
        returnList(newList)
    }
}

fun contactsToUser(
    allUsers: List<User>,
    contacts: List<Contact>,
    returnUserList: (MutableList<User>) -> Unit
) {
    val userlist = mutableListOf<User>()
    allUsers.forEach { user ->
        contacts.forEach { contact ->
            if (user.contacto == contact.numero) {
                userlist.add(user)
            }
        }
    }.also {
        returnUserList(userlist)
    }
}

fun validateQuestion(
    selectedColor: String,
    title: String,
    description: String,
    questionParticipantsIDList: MutableList<String>,
): Boolean {
    if (selectedColor != ""
        && title != ""
        && description != ""
        && questionParticipantsIDList.isNotEmpty()
    ) {
        return true
    }
    return false
}
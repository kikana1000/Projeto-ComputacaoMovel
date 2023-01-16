package pt.ipp.estg.doctorbrain.screens.app.groupsScreen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.data.GroupViewModel
import pt.ipp.estg.doctorbrain.models.Contact
import pt.ipp.estg.doctorbrain.models.Group
import pt.ipp.estg.doctorbrain.models.PhoneNumber
import pt.ipp.estg.doctorbrain.screens.sytemInputs.SelectGroupMembers
import pt.ipp.estg.doctorbrain.screens.sytemInputs.SimpleAlertDialog
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen

/**
 * Conteudo do Bottom Sheet da Informação de um Grup Selecionado*/
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfoGroupBottomSheetContent(
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    group: Group
) {
    val groupViewModel: GroupViewModel = viewModel()
    val allPhones = groupViewModel.getallPhoneNumber()?.observeAsState()
    val group_phones = mutableListOf<PhoneNumber>()
    if (allPhones != null) {
        if (allPhones.value != null) {
            allPhones.value!!.forEach {
                if (it.group == group._groupID) group_phones.add(it)
            }
        }
    }
    val show = remember { mutableStateOf(false) }
    val edit = remember { mutableStateOf(false) }
    SimpleAlertDialog(
        show = show.value,
        onConfirm = {
            groupViewModel.delete(group)
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
            show.value = false
        },
        onDismiss = { show.value = false },
        textTittle = "Delete Group?"
    )

    Column(Modifier.padding(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Group, "", Modifier.padding(5.dp))
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = group.Title, style = TitleofScreen())
                }
            }
            Row() {
                IconButton(onClick = {
                    edit.value = !edit.value
                }) {
                    Icon(Icons.Default.Edit, "Edit Group")
                }
                if (edit.value == false) {
                    IconButton(onClick = {
                        show.value = true
                    }) {
                        Icon(Icons.Default.Delete, "Delete Group")
                    }
                }
            }
        }
        if (!edit.value) {
            InfoContentGroup(group_phones, bottomSheetScaffoldState, coroutineScope)
        } else {
            val contacts = remember { mutableListOf<Contact>() }
            group_phones.forEach {
                contacts.add(Contact(it.name, it.phoneNumber))
            }
            val newgroup_phones = mutableListOf<PhoneNumber>()
            newgroup_phones.addAll(group_phones)
            EditContentGroup(group_phones, newgroup_phones, contacts, coroutineScope, group) {
                edit.value = false
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun EditContentGroup(
    oldgroup_phones: MutableList<PhoneNumber>,
    group_phones: MutableList<PhoneNumber>,
    contacts: MutableList<Contact>,
    coroutineScope: CoroutineScope,
    group: Group,
    onUpdate: () -> Unit
) {
    val context = LocalContext.current
    val groupViewModel: GroupViewModel = viewModel()
    Column(Modifier.padding(5.dp)) {
        val focusManager = LocalFocusManager.current
        var title by remember { mutableStateOf(group.Title) }
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 5.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Participants", style = InputLabel())
        }

        SelectGroupMembers(contacts) {
            contacts.removeAll(contacts)
            contacts.addAll(it)
        }
        Button(
            onClick = {
                if (validateTitle(title) && contacts.isNotEmpty()) {
                    validateTitle(title)
                    if (group.Title != title) {
                        group.Title = title
                    }
                    group_phones.clear()
                    contacts.forEach {
                        group_phones.add(PhoneNumber(group._groupID, it.nome, it.numero))
                    }
                    coroutineScope.launch {
                        Log.d("oldgroup_phones",oldgroup_phones.toString())
                        Log.d("group_phones",group_phones.toString())
                        groupViewModel.update(group, oldgroup_phones, group_phones)
                    }
                    onUpdate()
                }else{
                    Toast.makeText(context,"Empty Fields",Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "Update")
        }
    }
}

fun validateTitle(title: String): Boolean {
    if (title.length > 3) return true
    return false;
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InfoContentGroup(
    group_phones: MutableList<PhoneNumber>,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {
    DipalayPhonesInGrid(group_phones)
    Button(
        onClick = {
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = "Close")
    }
}

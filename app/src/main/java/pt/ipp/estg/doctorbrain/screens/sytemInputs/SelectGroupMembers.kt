package pt.ipp.estg.doctorbrain.screens.sytemInputs

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.doctorbrain.firestoreFun.FireBaseViewModel
import pt.ipp.estg.doctorbrain.models.Contact
import pt.ipp.estg.doctorbrain.models.User
import pt.ipp.estg.doctorbrain.screens.app.groupsScreen.ShowContacts
import pt.ipp.estg.doctorbrain.ui.theme.cardStyleModifier


/**
 * Não sei ...
 *
 * ADDS groups to the question
 * When i add a group ... it adds every participant the list of participants
 * When i add a participant ... it adds a participant if he has a userProfile


Fazer uma funcao q dado um numero de telefone, devolva o User q o tem
Modificar o selectGroupMember (duplicar) de forma a selecionar User s em vez de Contact

Tipo ... Não sei ...
Mais facil gravar o Group no firebase do que estar a tentar cruzar a info entre o firebase, o room e a lista de contactos ...
A gente precisa de fazer calls ao firebase para saber se existe um User com o contacto semelhante ao existente na lista de contactos ...
E precisa de saber qual é esse user para gravar o ID dos participantes nas questions (e nos grupos) ...
 */
@Composable
fun SelectGroupMembers(
    list: List<Contact>,
    returnList: (List<Contact>) -> Unit
) {
    val contacts = remember { mutableStateListOf<Contact>() }
    contacts.removeAll(contacts)
    contacts.addAll(list)
    val context = LocalContext.current
    val fireBaseViewModel: FireBaseViewModel = viewModel()
    val allUsersList = fireBaseViewModel.getUsers().observeAsState().value
    //Card com o contact picker
    Card(
        modifier = cardStyleModifier()
    ) {
        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            //Cena onde os contactos adicionados vao ser listados
            LazyVerticalGrid(
                columns = GridCells.Fixed(4), Modifier.padding(5.dp)
            ) {
                items(contacts) { contact ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        //Mudar funcao para showUser
                        ShowContacts(contact) {
                            contacts.remove(contact) //nao sei o q isto faz
                            returnList(contacts)
                        }
                    }
                }
            }
            //Pick Contact Button?
            ContactPickerTwinTurbo({ s1, s2 ->
                val newContact = Contact(s1, contactFormater(s2))
                if (s1 != "" && s2 != "" && !contacts.contains(newContact)) {
                    if (isContactInFireBase(newContact, allUsersList)) {
                        contacts.add(newContact)
                        returnList(contacts)
                    } else {
                        Toast.makeText(context, "Not in Doctor Brain", Toast.LENGTH_SHORT).show()
                        returnList(contacts)
                    }
                } else {
                    returnList(contacts)
                    Toast.makeText(context, "Already picked", Toast.LENGTH_SHORT).show()
                }
            }, {
                Toast.makeText(context, "No contact Selected", Toast.LENGTH_SHORT).show()
                returnList(contacts)
            })
        }
    }
}
@Composable
fun SelectGroupMembersTeste(
    list: List<Contact>,
    returnList: (List<Contact>, MutableList<User>?) -> Unit,
) {
    val contacts = remember { mutableStateListOf<Contact>() }
    contacts.removeAll(contacts)
    contacts.addAll(list)
    var addedUserList = mutableListOf<User>()

    val context = LocalContext.current
    val fireBaseViewModel: FireBaseViewModel = viewModel()
    val allUsersList = fireBaseViewModel.getUsers().observeAsState().value
    //Card com o contact picker
    Card(
        modifier = cardStyleModifier()
    ) {
        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            //Cena onde os contactos adicionados vao ser listados
            LazyVerticalGrid(
                columns = GridCells.Fixed(4), Modifier.padding(5.dp)
            ) {
                items(contacts) { contact ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        //Remover quando clico em cima do contacto
                        ShowContacts(contact) {
                            contacts.remove(contact)
                            addedUserList = getAddedUserList(allUsersList, contacts)
                            returnList(contacts, addedUserList)
                        }
                    }
                }
            }
            //Pick Contact Button?
            ContactPickerTwinTurbo({ s1, s2 ->
                val newContact = Contact(s1, contactFormater(s2))
                if (s1 != "" && s2 != "" && !contacts.contains(newContact)) {
                    if (isContactInFireBase(newContact, allUsersList)) {
                        contacts.add(newContact)
                        addedUserList = getAddedUserList(allUsersList, contacts)
                        returnList(contacts, addedUserList)
                    } else {
                        Toast.makeText(context, "Not in Doctor Brain", Toast.LENGTH_SHORT).show()
                        returnList(contacts, addedUserList)
                    }
                } else {
                    returnList(contacts, addedUserList)
                    Toast.makeText(context, "Already picked", Toast.LENGTH_SHORT).show()
                }
            }, {
                Toast.makeText(context, "No contact Selected", Toast.LENGTH_SHORT).show()
                returnList(contacts, addedUserList)
            })
        }
    }
}

/**
 * Formata uma string para a forma "+3519[0-9]{8}", aceite pelo firebase
 */
fun contactFormater(phoneNumber: String): String {
    phoneNumber.replace(" ", "")
    if (phoneNumber.length == 9) {
        return "+351".plus(phoneNumber)
    } else {
        if (phoneNumber.length == 12) return "351".plus(phoneNumber)
    }
    return phoneNumber
}

/**
 * Verifica se o contacto selecionado se encontra no FireBase
 * */
fun isContactInFireBase(newContact: Contact, allUsersList: List<User>?): Boolean {
    Log.d("allUsersList: List<User>?", allUsersList.toString())
    if (allUsersList != null) {
        for (user in allUsersList) {
            val contactoWithoutSpaces = newContact.numero.replace(" ", "")
            if (contactoWithoutSpaces == user.contacto) {
                return true
            }
        }
    }
    return false
}

/**
 * Verifica se o contacto selecionado se encontra no FireBase
 * */
fun getContactInFireBase(newContact: Contact, allUsersList: List<User>?): User? {
    Log.d("allUsersList: List<User>?", allUsersList.toString())
    if (allUsersList != null) {
        for (user in allUsersList) {
            val contactoWithoutSpaces = newContact.numero.replace(" ", "")
            if (contactoWithoutSpaces == user.contacto) {
                return user
            }
        }
    }
    return null
}



/**
 * Retorna Lista com IDs dos Users participantes
 */
@Composable
fun SelectGroupMembersForQuestions(onQuestionParticipantsSelected: (MutableList<User>) -> Unit) {
    val contactsList = remember { mutableStateListOf<Contact>() }
    val context = LocalContext.current

    //###############################################################
    //Para cada Contacts(nome, numero) em contactsList, vamos verificar se existe algum User em que Contacts.numero == User.contacto
    //Guardamos os Users adicionados numa lista

    val fireBaseViewModel: FireBaseViewModel = viewModel()
    val allUsersList = fireBaseViewModel.getUsers().observeAsState().value

    //###############################################################


    //Card com o contact picker
    Card(
        modifier = cardStyleModifier()
    ) {
        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            //Cena onde os contactos adicionados vao ser listados
            LazyVerticalGrid(
                columns = GridCells.Fixed(4), Modifier.padding(5.dp)
            ) {
                items(contactsList) { contact ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        //Mudar funcao para showUser
                        ShowContacts(contact) {
                            contactsList.remove(contact) //nao sei o q isto faz
                            onQuestionParticipantsSelected(
                                getAddedUserList(
                                    allUsersList,
                                    contactsList
                                )
                            )
                        }
                    }
                }
            }

            //Pick Contact Button?
            ContactPickerTwinTurbo({ s1, s2 ->
                val newContact = Contact(s1, contactFormater(s2))
                if (s1 != "" && s2 != "" && !contactsList.contains(newContact)) {
                    //MeterFuncao ... se existir User com o numero de telefone igual, adicionar a lista
                    if (isContactInFireBase(newContact, allUsersList)) {
                        contactsList.add(newContact)
                    }
                    onQuestionParticipantsSelected(getAddedUserList(allUsersList, contactsList))
                } else {
                    Toast.makeText(context, "Already picked", Toast.LENGTH_SHORT).show()
                }
            }, { Toast.makeText(context, "No contact Selected", Toast.LENGTH_SHORT).show() })
        }
    }
}

@Composable
@Preview
fun SelectGroupPreview() {
    val list = remember{ mutableStateListOf<Contact>() }
    SelectGroupMembers(list,{ })
}


/*
* Returns a MutableList com os UserProfiles escolhidos
*/
fun getAddedUserList(
    allUsersList: List<User>?,
    contactsList: List<Contact>
): MutableList<User> {
    val addedUserList = mutableListOf<User>() //importante
    if (allUsersList != null) {
        addedUserList.clear()
        for (contact in contactsList) {
            for (user in allUsersList) {
                val numberWithoutSpaces = contact.numero.replace(" ", "")
                Log.e("Contact Check!", "${numberWithoutSpaces} =? ${user.contacto}")
                if (numberWithoutSpaces.equals(user.contacto)) {
                    Log.d("PickedUserExiste", user.toString())
                    addedUserList.add(user)
                }
            }
        }
    } else {
        Log.e("UserNAOExiste?", allUsersList.toString())
    }
    return addedUserList
}




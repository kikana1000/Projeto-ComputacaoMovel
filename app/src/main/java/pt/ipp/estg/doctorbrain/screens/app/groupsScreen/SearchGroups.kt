package pt.ipp.estg.doctorbrain.screens.app.groupsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.doctorbrain.data.GroupViewModel
import pt.ipp.estg.doctorbrain.models.Group
import pt.ipp.estg.doctorbrain.ui.theme.InputLabel
import pt.ipp.estg.doctorbrain.ui.theme.cardStyleModifier


/**
 * Recebe uma String e retorna gurpos que tenham um nome parecido
 * @param search @param[search] string a procurar
 * @param returnGroup @param[returnGroup] lista que de grupo com titulo parecido à string passo
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchGroups(search: String, returnGroup: (Group) -> Unit) {
    val groupViewModel: GroupViewModel = viewModel()
    val allGroups = groupViewModel.getallGroups()?.observeAsState()
    val searchGroups = groupViewModel.search(search)?.observeAsState()
    Column(Modifier.verticalScroll(rememberScrollState())) {
        if (search != "") {
            if (searchGroups?.value != null) {
                searchGroups.value!!.forEach { group ->
                    Card(
                        modifier = cardStyleModifier(),
                        onClick = { returnGroup(group) }
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Group, "", Modifier.padding(5.dp))
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = group.Title, style = InputLabel())
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (allGroups?.value != null) {
                allGroups.value!!.forEach { group ->
                    Card(
                        modifier = cardStyleModifier(),
                        onClick = { returnGroup(group) }
                    ) {
                        Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Group, "", Modifier.padding(5.dp))
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = group.Title, style = InputLabel())
                                }
                            }
                        }
                    }
                }
            }
        }}
}
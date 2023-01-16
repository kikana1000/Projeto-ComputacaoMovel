package pt.ipp.estg.doctorbrain.screens.app.calendarScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.doctorbrain.data.SchoolClassViewModel
import pt.ipp.estg.doctorbrain.models.SchoolClass

/**
 * Usa viewModel para procurar na base de dados SchoolClasses por parametros parecidos a search
 *
 * @param search @param[search] String a procurar na base de dados
 * @param moreinfo @param[moreinfo] Usado para fazer o return da Lista de Objetos correspondentes
 * */
@Composable
fun SearchSchoolClasses(search: String, moreinfo: (SchoolClass) -> Unit) {
    val schoolClassViewModel: SchoolClassViewModel = viewModel()
    var classes = schoolClassViewModel.getAllSchoolClass().observeAsState()
    Column(modifier = Modifier.padding(top = 10.dp)) {
        if (search != "") {
            classes = schoolClassViewModel.search(search).observeAsState()
        }
        classes.value?.forEach {
            SchoolCard(sclass = it, moreinfo)
        }
    }
}

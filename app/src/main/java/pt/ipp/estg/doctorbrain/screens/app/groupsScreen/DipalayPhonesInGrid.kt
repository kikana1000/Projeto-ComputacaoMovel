package pt.ipp.estg.doctorbrain.screens.app.groupsScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipp.estg.doctorbrain.models.PhoneNumber

/***
 * Display de uma lista de PhoneNumbers em gird de 4 elementos
 * @param group_phones @param[group_phones] Lista de PhoneNumbers a fazer display
 */
@Composable
fun DipalayPhonesInGrid(group_phones: MutableList<PhoneNumber>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4), Modifier.padding(5.dp)
    ) {
        items(group_phones) { phones ->
            ShowContacts(phones = phones) {}
        }
    }
}
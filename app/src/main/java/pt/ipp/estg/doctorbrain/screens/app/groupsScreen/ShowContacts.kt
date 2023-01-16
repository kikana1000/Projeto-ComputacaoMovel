package pt.ipp.estg.doctorbrain.screens.app.groupsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.ipp.estg.doctorbrain.models.Contact
import pt.ipp.estg.doctorbrain.models.PhoneNumber

/**
 * Display 1 Phone Number
 * @param phones @param[phones] PhoneNumber a dar display
 * @param button @param[button] onClick trigger
 */
@Composable
fun ShowContacts(phones: PhoneNumber, button: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Button(
            onClick = { },
            modifier = Modifier.size(50.dp),  //avoid the oval shape
            shape = CircleShape,
        ) {
            IconButton(onClick = { button() }) {
                Icon(
                    Icons.Default.AccountCircle,
                    "Contact Icon"
                )
            }
        }
        Text(text = phones.name, textAlign = TextAlign.Center)
    }
}


/**
 * Display 1 Phone Number
 * @param phones @param[phones] Contacts a dar display
 * @param button @param[button] onClick trigger
 */
@Composable
fun ShowContacts(phones: Contact, button: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Button(
            onClick = { },
            modifier = Modifier.size(50.dp),  //avoid the oval shape
            shape = CircleShape,
        ) {
            IconButton(onClick = { button() }) {
                Icon(
                    Icons.Default.AccountCircle,
                    "Contact Icon"
                )
            }
        }
        Text(text = phones.nome, textAlign = TextAlign.Center)
    }
}
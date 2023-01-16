package pt.ipp.estg.doctorbrain.screens.sytemInputs

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import pt.ipp.estg.doctorbrain.ui.theme.primary

@Composable
fun SimpleAlertDialog(
    show: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    textTittle: String,
) {
    if(show){
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = "OK", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = "Cancel", color = Color.White) }
            },
            title = { Text(text = textTittle) },
            backgroundColor = primary,
            contentColor = Color.White,
        )
    }
}
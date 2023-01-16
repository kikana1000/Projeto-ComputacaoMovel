package pt.ipp.estg.doctorbrain.screens.sytemInputs

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DialogViewModel: ViewModel() {
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    fun onOpenDialogClicked() {
        _showDialog.value = true

    }

    fun onDialogConfirm() {
        _showDialog.value = false
        // Continue with executing the confirmed action
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

}
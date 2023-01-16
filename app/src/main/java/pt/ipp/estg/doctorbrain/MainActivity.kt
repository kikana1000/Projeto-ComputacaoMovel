package pt.ipp.estg.doctorbrain

import RootGraph
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.FirebaseMessagingKtxRegistrar
import pt.ipp.estg.doctorbrain.firestoreFun.FireBaseViewModel
import pt.ipp.estg.doctorbrain.notifications.pushNotification.FirebaseService
import pt.ipp.estg.doctorbrain.ui.theme.DoctorBrainTheme


/**
 * Main activity do programa
 */
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { FirebaseService.token=it}

        setContent {
            DoctorBrainTheme {
                RootGraph(rememberNavController())
                //appGraph(rememberNavController(), auth, db)
            }
        }
    }
}


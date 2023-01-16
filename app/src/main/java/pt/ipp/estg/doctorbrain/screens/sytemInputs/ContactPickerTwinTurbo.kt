package pt.ipp.estg.doctorbrain.screens.sytemInputs

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Retorna o Nome e numero de Telemovel de um contact Selecionado
 */
@SuppressLint("Range", "Recycle")
@Composable
fun ContactPickerTwinTurbo(
    done: (String, String) -> Unit,
    makeErrorToast:()->Unit
) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact(),
        onResult = {
            val contentResolver: ContentResolver = context.contentResolver
            val name: String
            var number = ""
            try {
                val cursor: Cursor? = contentResolver.query(it!!, null, null, null, null)
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        Log.d("Name", name)
                        val id =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        val phones: Cursor? = contentResolver.query(
                            Phone.CONTENT_URI, null,
                            Phone.CONTACT_ID + " = " + id, null, null
                        )
                        if (phones != null) {
                            while (phones.moveToNext()) {
                                number = phones.getString(phones.getColumnIndex(Phone.NUMBER))
                            }
                            done(name, number)
                            phones.close()
                        }
                    }
                }
            } catch (e: RuntimeException) {
                Log.d("ERRO",e.message.toString())
                makeErrorToast()
            }
        },
    )
    Button(
        onClick = {
            if (hasContactPermission(context)) launcher.launch()
            else {
                requestContactPermission(context, activity)
                launcher.launch()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = "Pick Contact")
    }
}

fun hasContactPermission(context: Context): Boolean {
    // on below line checking if permission is present or not.
    return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED
}

fun requestContactPermission(context: Context, activity: Activity) {
    // on below line if permission is not granted requesting permissions.
    if (!hasContactPermission(context)) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), 1)
    }
}
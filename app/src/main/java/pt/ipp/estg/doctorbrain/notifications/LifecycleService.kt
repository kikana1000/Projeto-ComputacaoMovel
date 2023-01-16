package pt.ipp.estg.doctorbrain.notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder

open class LifecycleService : Service() {
    //return null for tutorial only.
    override fun onBind(p0: Intent?): IBinder? = null
}
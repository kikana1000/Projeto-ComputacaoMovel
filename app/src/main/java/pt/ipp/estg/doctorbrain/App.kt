package pt.ipp.estg.doctorbrain

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build


class App : Application() {

    val SCHOOL_CHANNEL_ID = "SCHOOL_SERVICE_CHANNEL"
    val EVENT_CHANNEL_ID = "EVENT_SERVICE_CHANNEL"
    val PUSH_CHANNEL_ID = "PUSH_SERVICE_CHANNEL"
    private fun createEventNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                EVENT_CHANNEL_ID,
                "Event Notifications",
                NotificationManager.IMPORTANCE_DEFAULT

            )
            serviceChannel.enableLights(true)
            serviceChannel.lightColor= Color.WHITE
            serviceChannel.enableVibration(true)
            serviceChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
    private fun createPushNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                PUSH_CHANNEL_ID,
                "Push Notifications",
                NotificationManager.IMPORTANCE_DEFAULT

            )
            serviceChannel.enableLights(true)
            serviceChannel.lightColor= Color.WHITE
            serviceChannel.enableVibration(true)
            serviceChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
    private fun createSchoolClassNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                SCHOOL_CHANNEL_ID,
                "School Classes Notifications",
                NotificationManager.IMPORTANCE_DEFAULT

            )
            serviceChannel.enableLights(true)
            serviceChannel.lightColor= Color.WHITE
            serviceChannel.enableVibration(true)
            serviceChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
    companion object {
        var ctx: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        createPushNotificationChannel()
        createEventNotificationChannel()
        createSchoolClassNotificationChannel()
        ctx = applicationContext
    }

}
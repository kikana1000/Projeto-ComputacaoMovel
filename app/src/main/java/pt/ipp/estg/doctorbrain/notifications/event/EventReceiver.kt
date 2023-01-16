@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)

package pt.ipp.estg.doctorbrain.notifications.event

import android.app.Notification
import android.app.NotificationManager

import android.content.Context
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.*
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import pt.ipp.estg.doctorbrain.App
import pt.ipp.estg.doctorbrain.MainActivity
import pt.ipp.estg.doctorbrain.R
import java.util.*
import java.util.Calendar.*

/**
 * Receiver que recebe o alarme para notificar os Eventos
 */
const val notificationId=2
const val channelId="EVENT_SERVICE_CHANNEL"
const val EVENT="Event"
const val messageExtra="Event will start!!"
const val DATE =""
class EventReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val toastText = String.format("Alarm Reboot")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            startRescheduleAlarmsService(context);
        } else {
            val toastText = String.format("Alarm Received")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            intent.getStringExtra(DATE)
            Log.d("Notification", intent.getStringExtra(DATE)!!)
                if (alarmIsToday(intent)) {
                    Log.d("Notification", intent.getStringExtra(DATE)!!)
                    startAlarmService(context, intent)
                }
            }
        }
    }
    private fun alarmIsToday(intent: Intent): Boolean {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        val day: Int = calendar.get(DAY_OF_MONTH)
        val month: Int = (calendar.get(MONTH) +1)
        val year: Int =calendar.get(YEAR)
        val date= intent.getStringExtra(DATE)
        val datenow = "$day/$month/$year"
        Log.d("Notification2", datenow)
        if(date.equals(datenow)){
            return true
        }

        return false
    }
    private fun startAlarmService(context: Context, intent: Intent) {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent2 = Intent(App.ctx!!, MainActivity::class.java)
        val alarmPendingIntent = PendingIntent.getActivity(App.ctx!!, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmTitle = java.lang.String.format("%s is starting!!", intent!!.getStringExtra(
            EVENT
        ))
        val notification: Notification
        var notiBuilder  = NotificationCompat.Builder(App.ctx!!, channelId)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
            .setContentTitle(alarmTitle)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(soundUri)
            .setOnlyAlertOnce(true)
            .setContentText("Your next event is due date!!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(alarmPendingIntent)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notiBuilder.setSmallIcon(R.drawable.ic_notification_icon)
            notiBuilder.setColor(context.resources.getColor(R.color.ic_launcher_background))
        } else {
            notiBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
        }
        notification= notiBuilder
            .build()
        val alarmManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        alarmManager.notify(Random().nextInt(Integer.MAX_VALUE),notification)
    }

   private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleEventService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

package pt.ipp.estg.doctorbrain.notifications.pushNotification


import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pt.ipp.estg.doctorbrain.App
import pt.ipp.estg.doctorbrain.MainActivity
import pt.ipp.estg.doctorbrain.R
import java.util.*


const val channelId="PUSH_SERVICE_CHANNEL"

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
class FirebaseService : FirebaseMessagingService() {

    companion object{
        var sharedPref:SharedPreferences?=null
        var token:String?
        get(){
            return sharedPref?.getString("token","")
        }
        set(value){
            sharedPref?.edit()?.putString("token",value)?.apply()
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        token=p0
    }
    override fun onMessageReceived(message: RemoteMessage) {
        if(message.getNotification()!=null){
            if(message.data!!.get("sender")!! !=null) {
                generateNotificationQuestion(
                    message.notification!!.title!!,
                    message.notification!!.body!!,
                    message.data!!.get("sender")!!,
                    message.data!!.get("question")!!
                )
            }else{
                generateNotification(
                    message.notification!!.title!!,
                    message.notification!!.body!!)
            }
        }
    }

    fun generateNotificationQuestion(title:String, message:String,from:String,question:String){
        val id= Firebase.auth.currentUser!!.uid
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        Log.d("OLA",question)
        val taskDetailIntent = Intent(
            Intent.ACTION_VIEW,
            "https://www.doctorbrain.com/selectedQuestionID=${question}".toUri(),
            App.ctx!!,
            MainActivity::class.java
        )

        var pending: PendingIntent = TaskStackBuilder.create(App.ctx!!).run {
            addNextIntentWithParentStack(taskDetailIntent)
            getPendingIntent(2, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        if(id==null){
            val intent2 = Intent(App.ctx!!, MainActivity::class.java)
            pending=PendingIntent.getActivity(App.ctx!!, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val notification: Notification
        val grupo= "answers"
        var notiBuilder  = NotificationCompat.Builder(App.ctx!!, channelId)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
            .setContentTitle(title)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(soundUri)
            .setOnlyAlertOnce(true)
            .setGroup(grupo)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pending)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notiBuilder.setSmallIcon(R.drawable.ic_notification_icon)
            notiBuilder.setColor(resources.getColor(R.color.ic_launcher_background))
        } else {
            notiBuilder.setSmallIcon(R.drawable.ic_notification_icon)
        }
        notification= notiBuilder.build()
        val alarmManager = App.ctx!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(id!=from) {
            alarmManager.notify(Random().nextInt(Integer.MAX_VALUE), notification)
        }
    }
    fun generateNotification(title:String, message:String){
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent2 = Intent(App.ctx!!, MainActivity::class.java)
        val notification: Notification
            val alarmPendingIntent =
                PendingIntent.getActivity(App.ctx!!, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)
        var notiBuilder  = NotificationCompat.Builder(this, channelId)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
            .setContentTitle(title)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(soundUri)
            .setOnlyAlertOnce(true)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(alarmPendingIntent)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notiBuilder.setSmallIcon(R.drawable.ic_notification_icon)
            notiBuilder.setColor(resources.getColor(R.color.ic_launcher_background))
        } else {
            notiBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
        }
        notification= notiBuilder
                .build()

        val alarmManager = App.ctx!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        alarmManager.notify(Random().nextInt(Integer.MAX_VALUE), notification)

    }
}
package pt.ipp.estg.doctorbrain.models

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ipp.estg.doctorbrain.notifications.event.EventReceiver
import pt.ipp.estg.doctorbrain.notifications.event.DATE
import pt.ipp.estg.doctorbrain.notifications.event.EVENT
import pt.ipp.estg.doctorbrain.notifications.schoolClass.SCHOOLCLASS
import java.util.*

/**
 * Modelo de Dados de um Event
 */
@Entity(tableName = "singleEvent")
data class SingleEvent(
    var startHour:String,
    var finishtHour:String,
    var date: String,
    var color: String,
    override var title: String,
    override var local: String,
):Event(){
    @PrimaryKey(autoGenerate = true)
    override var _eventID: Int = 0
    var started :Boolean= false
    var not_id :Int = Random().nextInt(Int.MAX_VALUE)


    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, EventReceiver::class.java)
        intent.putExtra(EVENT, title)
        intent.putExtra(DATE,date)
        val hour = Integer.parseInt(startHour.split(":")[0])
        val minu = Integer.parseInt(startHour.split(":")[1])
        val day = Integer.parseInt(date.split("/")[0])
        val month = Integer.parseInt(date.split("/")[1])
        val year =Integer.parseInt(date.split("/")[2])
        val alarmPendingIntent = PendingIntent.getBroadcast(context, this.not_id, intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.DAY_OF_MONTH,day)
        calendar.set(Calendar.MONTH,month-1)
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minu)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            this.started = false
            return
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmPendingIntent
        )

        this.started = true
    }


    fun cancelSchedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, EventReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, this.not_id, intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.cancel(alarmPendingIntent)
        this.started=false
    }
}
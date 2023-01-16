package pt.ipp.estg.doctorbrain.models

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ipp.estg.doctorbrain.models.enums.StringToWeekDay
import pt.ipp.estg.doctorbrain.notifications.schoolClass.SCHOOLCLASS
import pt.ipp.estg.doctorbrain.notifications.schoolClass.SchoolReceiver
import pt.ipp.estg.doctorbrain.notifications.schoolClass.WEEKDAYS
import java.util.*


/**
 * Modelo de Dados de um dia/hora de uma SchoolClass
 */
@Entity(tableName = "hourDateSchoolClass")
data class HourDateSchoolClass(
    @ColumnInfo(name = "schoolClass")
    var schooClass:Int,
    var startHour:String,
    var finishtHour:String,
    var weekDays: String,
    var started:Boolean
){
    @PrimaryKey(autoGenerate = true)
    var _id :Int = 0

    var not_id :Int =Random().nextInt(Int.MAX_VALUE)


    fun schedule(context: Context, title:String) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SchoolReceiver::class.java)
        intent.putExtra(SCHOOLCLASS, title)
        intent.putExtra(WEEKDAYS,weekDays)
        val hour = Integer.parseInt(startHour.split(":")[0])
        val minu = Integer.parseInt(startHour.split(":")[1])
        val alarmPendingIntent = PendingIntent.getBroadcast(context, this.not_id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.DAY_OF_WEEK, StringToWeekDay( weekDays));
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minu)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val now = Calendar.getInstance()
        now[Calendar.SECOND] = 0
        now[Calendar.MILLISECOND] = 0

        if (calendar.before(now)) {    //this condition is used for future reminder that means your reminder not fire for past time
            calendar.add(Calendar.DATE, 7);
        }

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                7 *24 * 60 * 60 * 1000 ,
                alarmPendingIntent
            )

        this.started = true
    }


    fun cancelSchedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, SchoolReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, this.not_id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(alarmPendingIntent)
        this.started = false;
    }


}
package pt.ipp.estg.doctorbrain.notifications.event

import android.content.Intent

import android.os.IBinder
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.*
import pt.ipp.estg.doctorbrain.data.HourDateRepository
import pt.ipp.estg.doctorbrain.data.RoomDataBase
import pt.ipp.estg.doctorbrain.data.SchoolClassRepository
import pt.ipp.estg.doctorbrain.data.SingleEventRepository
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.models.SingleEvent
import pt.ipp.estg.doctorbrain.notifications.LifecycleService
import javax.annotation.Nullable


class RescheduleEventService : LifecycleService(),LifecycleOwner{
    private val mServiceLifecycleDispatcher = ServiceLifecycleDispatcher(this)
    val db = RoomDataBase.getInstace(application)
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val eventRepository =SingleEventRepository(db.SingleEventDao())
        val schoolClasses=eventRepository.getSingleEvents()
        schoolClasses.observe(this) {

            fun onChanged(alarms: List<SingleEvent>) {
                for (a in alarms) {
                        if(a.started){
                            a.schedule(application)
                        }
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        mServiceLifecycleDispatcher.onServicePreSuperOnBind()
        return null;
    }

    override fun getLifecycle() = mServiceLifecycleDispatcher.lifecycle
}
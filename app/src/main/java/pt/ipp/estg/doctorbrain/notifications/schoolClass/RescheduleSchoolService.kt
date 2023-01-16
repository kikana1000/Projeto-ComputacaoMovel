package pt.ipp.estg.doctorbrain.notifications.schoolClass

import android.content.Intent

import android.os.IBinder
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.*
import pt.ipp.estg.doctorbrain.data.HourDateRepository
import pt.ipp.estg.doctorbrain.data.RoomDataBase
import pt.ipp.estg.doctorbrain.data.SchoolClassRepository
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.notifications.LifecycleService
import javax.annotation.Nullable


class RescheduleSchoolService : LifecycleService(),LifecycleOwner{
    private val mServiceLifecycleDispatcher = ServiceLifecycleDispatcher(this)
    val db = RoomDataBase.getInstace(application)
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val schoolRepository = SchoolClassRepository(db.SchoolClassDao())
        val schoolClasses=schoolRepository.getSchoolClasses()
        val HourDateSchoolClass =HourDateRepository(db.HourDateDao())
        schoolClasses.observe(this) {

            fun onChanged(alarms: List<SchoolClass>) {
                for (a in alarms) {
                    val hourDates= HourDateSchoolClass.getHourDateBySchooClass(a._eventID)
                    for(i in hourDates.value!!){
                        if(i.started){
                            i.schedule(application,a.title)
                        }
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
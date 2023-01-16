package pt.ipp.estg.doctorbrain.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass

/**
 * Implementação do DAO(Data Access Object) dos dia/hora quando a schoolClass acontece
 */
class HourDateRepository(val hourDateDao: HourDateDao) {
    fun insertHour(newHourDateSchoolClass: HourDateSchoolClass){
        hourDateDao.insertHourDate(newHourDateSchoolClass)
    }

    fun deleteHourDate(hourDateSchoolClass: HourDateSchoolClass){
        hourDateDao.deleteHourDate(hourDateSchoolClass)
    }

    fun deleteHoursById(id: Int){
        hourDateDao.deleteHourDateBySchoolClassID(id)
    }

    fun getHourDateBySchooClass(id:Int): LiveData<List<HourDateSchoolClass>> {
        return hourDateDao.getHourDateBySchooClass(id)
    }

    fun getAllHours(): LiveData<List<HourDateSchoolClass>> {
        return hourDateDao.getAllHourDate()
    }

}
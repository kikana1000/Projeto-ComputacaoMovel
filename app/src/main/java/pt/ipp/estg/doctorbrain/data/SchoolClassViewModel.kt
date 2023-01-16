package pt.ipp.estg.doctorbrain.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass
import pt.ipp.estg.doctorbrain.models.SchoolClass

/**
 * Modelo de view da SchoolClass
 */
class SchoolClassViewModel(application: Application) : AndroidViewModel(application) {

    val repositorySchoolClass: SchoolClassRepository
    val repositoryHourDate: HourDateRepository
    var searchresults: LiveData<List<SchoolClass>>
    var hoursOfId: LiveData<List<HourDateSchoolClass>>?
    var context =(application as Context)
    init {
        val db = RoomDataBase.getInstace(application)
        repositorySchoolClass = SchoolClassRepository(db.SchoolClassDao())
        repositoryHourDate = HourDateRepository(db.HourDateDao())
        searchresults = getAllSchoolClass()
        hoursOfId = null
    }

    fun addSchoolClass  (
        schoolClass: SchoolClass,
        listHours: List<HourDateSchoolClass>,
        count: Int
    ) {
        var id: Long = -1
        viewModelScope.launch(Dispatchers.IO) {
            id.let {
                id = repositorySchoolClass.insertSchoolClass(schoolClass)
            }
            for (i in 0..count) {
                listHours[i].schooClass = id.toInt()
                addHourDate(listHours[i])
            }

        }
    }

    fun update(
        schoolClass: SchoolClass,
        listHoursnew: List<HourDateSchoolClass>,
        count: Int,
        listHoursold: List<HourDateSchoolClass>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repositorySchoolClass.updateSchoolClass(schoolClass)
        }
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ViewModel[List]", listHoursold.toString())
            if (!listHoursnew.isEmpty()){
            val ids = mutableListOf<Int>()
            for (i in 0..count){
                listHoursnew[i].schooClass = schoolClass._eventID
                ids.add(listHoursnew[i]._id)
                repositoryHourDate.insertHour(listHoursnew[i])
            }
            listHoursold.forEach {
                if (!ids.contains(it._id)) {
                    repositoryHourDate.deleteHourDate(it)
                }
            }}
        }
    }

    fun find(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repositorySchoolClass.findbyId(id)
        }
    }

    fun search(search: String): LiveData<List<SchoolClass>> {
        viewModelScope.async {
            searchresults = repositorySchoolClass.search(search)
        }
        return searchresults
    }

    fun deleteSchoolClass(schoolClass: SchoolClass) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryHourDate.deleteHoursById(schoolClass._eventID)
            repositorySchoolClass.deleteSchoolClass(schoolClass)
        }
    }

    fun getAllSchoolClass(): LiveData<List<SchoolClass>> {
        return repositorySchoolClass.getSchoolClasses()
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repositorySchoolClass.deleteAll()
        }
    }

    private fun addHourDate(newHourDateSchoolClass: HourDateSchoolClass) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryHourDate.insertHour(newHourDateSchoolClass)
        }
    }

    fun getHourDateBySchooClass(id: Int): LiveData<List<HourDateSchoolClass>> {
        viewModelScope.async {
            hoursOfId = repositoryHourDate.getHourDateBySchooClass(id)
        }
        return hoursOfId!!
    }

    fun getAllHourDate(): LiveData<List<HourDateSchoolClass>> {
        var allhour: LiveData<List<HourDateSchoolClass>>? = null
        viewModelScope.async {
            allhour = repositoryHourDate.getAllHours()
        }
        return allhour!!
    }

}
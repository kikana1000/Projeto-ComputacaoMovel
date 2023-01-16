package pt.ipp.estg.doctorbrain.data

import android.icu.text.StringSearch
import androidx.lifecycle.LiveData
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.models.SingleEvent
/**
 * Implementação do DAO(Data Access Object) dos Events
 */
class SingleEventRepository(val singleEventDao: SingleEventDao) {

    suspend fun insertSingleEvent(newSingleEvent: SingleEvent) {
        singleEventDao.InsertSingleEvent(newSingleEvent)
    }

    suspend fun updateSingleEvent(singleEvent: SingleEvent){
        singleEventDao.updateSingleEvent(singleEvent)
    }

    suspend fun deleteSingleEvent(singleEvent: SingleEvent) {
        singleEventDao.DeleteSingleEvent(singleEvent)
    }

    suspend fun deleteAll() {
        singleEventDao.DeleteAllSingleEvent()
    }

    fun findbyId(id:Int):LiveData<List<SingleEvent>>{
        return singleEventDao.FindSingleEvent(id);
    }

    fun search(search: String):LiveData<List<SingleEvent>>{
        return singleEventDao.SearchSingleEvent(search)
    }

    fun getSingleEvents():LiveData<List<SingleEvent>>{
        return singleEventDao.GetAll()
    }
}
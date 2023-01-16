package pt.ipp.estg.doctorbrain.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.models.SingleEvent

/**
 * Modelo de view da SchoolClass
 */
class SingleEventViewModel(application: Application) : AndroidViewModel(application) {

    val repository: SingleEventRepository
    var searchresults: LiveData<List<SingleEvent>>
    init {
        val db = RoomDataBase.getInstace(application)
        repository = SingleEventRepository(db.SingleEventDao())
        searchresults = getAll()
    }

    fun addSingleEvent(singleEvent: SingleEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSingleEvent(singleEvent)
        }
    }

    fun updateSingleEvent(singleEvent: SingleEvent){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSingleEvent(singleEvent)
        }
    }

    fun find(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.findbyId(id)
        }
    }

    fun search(search: String): LiveData<List<SingleEvent>> {
        viewModelScope.async {
            searchresults = repository.search(search)
        }
        return searchresults
    }

    fun deleteSingleEvent(singleEvent: SingleEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSingleEvent(singleEvent)
        }
    }

    fun getAll(): LiveData<List<SingleEvent>> {
        return repository.getSingleEvents()
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

}
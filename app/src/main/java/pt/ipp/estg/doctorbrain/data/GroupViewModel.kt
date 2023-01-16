package pt.ipp.estg.doctorbrain.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pt.ipp.estg.doctorbrain.models.Contact
import pt.ipp.estg.doctorbrain.models.Group
import pt.ipp.estg.doctorbrain.models.PhoneNumber


class GroupViewModel(application: Application) : AndroidViewModel(application) {

    private val repositoryPhoneNumber: PhoneNumberRepository
    private val repositoryGroup: GroupRepository

    init {
        val db = RoomDataBase.getInstace(application)
        repositoryGroup = GroupRepository(db.GroupDao())
        repositoryPhoneNumber = PhoneNumberRepository(db.PhoneDao())
    }

    fun insert(group: Group, list: List<Contact>) {
        var id: Long = -1
        viewModelScope.launch(Dispatchers.IO) {
            id.let {
                id = repositoryGroup.insert(group)
            }
            list.forEach {
                Log.d("::", it.toString())
                repositoryPhoneNumber.insert(PhoneNumber(id.toInt(), it.nome, it.numero))
            }
        }
    }

    fun delete(group: Group) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryGroup.delete(group)
            repositoryPhoneNumber.deletebyID(group._groupID)
        }
    }

    fun update(group: Group, oldlist: List<PhoneNumber>,newlist: List<PhoneNumber>) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryGroup.update(group)
            //Elemiar contactos que já nao pertencem à lista
            oldlist.forEach { oldContact ->
                var exist = false
                newlist.forEach { newContact ->
                    if (oldContact.phoneNumber == newContact.phoneNumber){
                        exist = true
                    }
                }
                if (!exist){
                    repositoryPhoneNumber.delete(oldContact)
                }
            }
            //Adocionar contactos novos
            newlist.forEach { newContact ->
                var exist = false
                oldlist.forEach { oldContact ->
                    if (oldContact.phoneNumber == newContact.phoneNumber){
                        exist = true
                    }
                }
                if (!exist){
                    repositoryPhoneNumber.insert(newContact)
                }
            }
        }
    }

    fun getallGroups(): LiveData<List<Group>>? {
        var allGroup: LiveData<List<Group>>? = null
        viewModelScope.async {
            allGroup = repositoryGroup.getall()
        }
        return allGroup
    }

    fun getallPhoneNumber(): LiveData<List<PhoneNumber>>? {
        var allPhoneNumber: LiveData<List<PhoneNumber>>? = null
        viewModelScope.async {
            allPhoneNumber = repositoryPhoneNumber.getall()

        }
        return allPhoneNumber
    }

    fun search(search: String): LiveData<List<Group>>? {
        var searchGroup: LiveData<List<Group>>? = null
        viewModelScope.async {
            searchGroup = repositoryGroup.search(search)
        }
        return searchGroup
    }
}
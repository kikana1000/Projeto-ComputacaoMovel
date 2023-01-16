package pt.ipp.estg.doctorbrain.data
import androidx.lifecycle.LiveData
import pt.ipp.estg.doctorbrain.models.Group

class GroupRepository(val groupdao:GroupDao) {

    fun insert(group:Group):Long{
        return groupdao.insert(group)
    }

    fun delete(group:Group){
        groupdao.delete(group)
    }

    fun update(group:Group){
        groupdao.update(group)
    }

    fun getall(): LiveData<List<Group>> {
        return groupdao.getAll()
    }

    fun search(search:String): LiveData<List<Group>>{
        return groupdao.search(search)
    }
}
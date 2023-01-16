package pt.ipp.estg.doctorbrain.data
import androidx.lifecycle.LiveData
import pt.ipp.estg.doctorbrain.models.PhoneNumber

class PhoneNumberRepository(val phoneDao: PhoneDao) {

    fun insert(phoneNumber: PhoneNumber){
        phoneDao.insert(phoneNumber)
    }

    fun delete(phoneNumber: PhoneNumber){
        phoneDao.delete(phoneNumber)
    }

    fun deletebyID(id:Int){
        getall().value?.forEach {
            if (it.group == id) delete(it)
        }
    }

    fun update(phoneNumber: PhoneNumber) {
        phoneDao.update(phoneNumber)
    }

    fun getall():LiveData<List<PhoneNumber>>{
        return phoneDao.getall()
    }

    fun getbyID(id:Int):Boolean{
        return phoneDao.getbyIid(id)
    }
}
package pt.ipp.estg.doctorbrain.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pt.ipp.estg.doctorbrain.models.PhoneNumber

@Dao
interface PhoneDao {
    @Insert
    fun insert(phoneNumber: PhoneNumber)

    @Delete
    fun delete(phoneNumber: PhoneNumber)

    @Update
    fun update(phoneNumber: PhoneNumber)

    @Query("select * from phoneNumber")
    fun getall(): LiveData<List<PhoneNumber>>

    @Query("select * from phoneNumber where :id = id")
    fun getbyIid(id:Int): Boolean
}
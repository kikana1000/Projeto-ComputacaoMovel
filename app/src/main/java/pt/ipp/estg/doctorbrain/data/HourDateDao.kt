package pt.ipp.estg.doctorbrain.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass

/**
 * Definição do DAO(Data Access Object) da dia/hora quando a schoolClass acontece
 */
@Dao
interface HourDateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHourDate(hourdate: HourDateSchoolClass)


    @Query("delete from hourDateSchoolClass where schoolClass = :eventID")
    fun deleteHourDateBySchoolClassID(eventID:Int)

    @Delete
    fun deleteHourDate(hourdate: HourDateSchoolClass)

    @Query("select * from hourDateSchoolClass where _id = :id")
    fun getHourDateById(id: Int): LiveData<List<HourDateSchoolClass>>

    @Transaction
    @Query("select * from hourDateSchoolClass where schoolClass = :eventID")
    fun getHourDateBySchooClass(eventID:Int): LiveData<List<HourDateSchoolClass>>

    @Query("select * from hourDateSchoolClass ")
    fun getAllHourDate(): LiveData<List<HourDateSchoolClass>>
}
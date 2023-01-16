package pt.ipp.estg.doctorbrain.data

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass
import pt.ipp.estg.doctorbrain.models.SchoolClass
/**
 * Definição do DAO(Data Access Object) das SchoolClass
 */
@Dao
interface SchoolClassDao {
    @Insert
    fun insertSchoolClass(schoolClass:SchoolClass):Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSchoolClass(schoolClass:SchoolClass)

    @Delete
    fun deleteSchoolClass(schoolClass: SchoolClass)
    @Query("select * from schoolClass where" +
            " title like '%' || :search  || '%' or" +
            " local like '%' || :search  || '%'"
    )
    fun searchSchoolClasses(search:String): LiveData<List<SchoolClass>>

    @Query("delete from schoolClass")
    fun deleteAllSchoolClass()

    @Query("select * from schoolClass where _eventID = :id")
    fun findSchoolClass(id: Int): LiveData<List<SchoolClass>>

    @Query("select * from schoolClass")
    fun getAllSchoolClass(): LiveData<List<SchoolClass>>
}

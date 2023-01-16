package pt.ipp.estg.doctorbrain.data

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.models.SingleEvent

/**
 * Definição do DAO(Data Access Object) dos Events
 */
@Dao
interface SingleEventDao {
    @Insert
    fun InsertSingleEvent(singleEvent:SingleEvent)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSingleEvent(singleEvent: SingleEvent)

    @Delete
    fun DeleteSingleEvent(singleEvent: SingleEvent)

    @Query("delete from singleEvent")
    fun DeleteAllSingleEvent()

    @Query("select * from singleEvent where" +
            " title like '%' || :search  || '%' or" +
            " local like '%' || :search  || '%' or" +
            " startHour like '%' || :search  || '%'"
    )
    fun SearchSingleEvent(search:String): LiveData<List<SingleEvent>>

    @Query("select * from singleEvent where _eventID = :id")
    fun FindSingleEvent(id: Int): LiveData<List<SingleEvent>>

    @Query("select * from singleEvent")
    fun GetAll(): LiveData<List<SingleEvent>>
}
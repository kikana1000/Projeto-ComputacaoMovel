package pt.ipp.estg.doctorbrain.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pt.ipp.estg.doctorbrain.models.Group

@Dao
interface GroupDao {
    @Insert
    fun insert(group: Group):Long

    @Delete
    fun delete(group: Group)

    @Update
    fun update(group: Group)

    @Query("select * from groups")
    fun getAll(): LiveData<List<Group>>

    @Query("select * from groups where Title like '%' || :search  || '%'")
    fun search(search:String): LiveData<List<Group>>
}
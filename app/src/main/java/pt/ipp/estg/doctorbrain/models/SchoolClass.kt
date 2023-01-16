package pt.ipp.estg.doctorbrain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de Dados de uma SchoolClass
 */
@Entity(tableName = "schoolClass")
data class SchoolClass(
    var color: String,
    override val title: String,
    override var local: String,
):Event(){
    @PrimaryKey(autoGenerate = true)
    override var _eventID: Int = 0
}
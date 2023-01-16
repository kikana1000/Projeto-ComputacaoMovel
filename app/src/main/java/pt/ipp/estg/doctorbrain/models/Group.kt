package pt.ipp.estg.doctorbrain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de Dados de um grupo
 */
@Entity(tableName = "groups")
data class Group(
    var Title: String,
) {
    @PrimaryKey(autoGenerate = true)
    var _groupID: Int = 0
}
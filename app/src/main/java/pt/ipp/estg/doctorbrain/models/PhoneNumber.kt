package pt.ipp.estg.doctorbrain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "phoneNumber",
    foreignKeys = [ForeignKey(
        entity = Group::class, parentColumns = arrayOf("_groupID"),
        childColumns = arrayOf("group"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class PhoneNumber(
    var group:Int,
    val name: String,
    val phoneNumber: String
) {
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
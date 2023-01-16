package pt.ipp.estg.doctorbrain.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Relation da tabela da SchoolClass e Hourdate
 */
data class R_SC_HD(
    @Embedded val schoolClass:SchoolClass,
    @Relation(
        parentColumn = "_eventID",
        entityColumn = "_eventID"
    )
    val hourDate:List<HourDateSchoolClass>
)
package pt.ipp.estg.doctorbrain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime
import pt.ipp.estg.doctorbrain.models.enums.State
import pt.ipp.estg.doctorbrain.models.enums.State.Open

/**
 * Modelo de Dados de uma Question
 */
@Entity
data class Question(
    @PrimaryKey var QuestionID: String = "myQuestionID",
    val OwnerID: String = "myOwnerID",
    var Title: String = "myTitle",
    var Description: String = "myDescription",
    var Date_and_Hour: DateTime? = null,
    var Answers: MutableList<Answer> = mutableListOf(),
    var Participants: MutableList<String> = mutableListOf(),
    var IsPutlic: Boolean = false,
    var State: State = Open,
) {
}
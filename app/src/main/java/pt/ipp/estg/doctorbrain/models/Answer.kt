package pt.ipp.estg.doctorbrain.models

import com.google.type.DateTime

/**
 * Modelo de Dados de um Answer
 */
data class Answer (
    val _ownerID: String = "myOwnerID",
    val _answerID: String = "myAnswerID",
    val Description: String = "myAnswerDescription",
    val Date_and_Hour: DateTime? = null,
    )
package pt.ipp.estg.doctorbrain.models

import androidx.room.Entity

/**
 * Modelo de Dados de um User
 */
@Entity(tableName = "user")
data class User(
    val id: String = "myUserID",
    var name: String = "myName",
    var email: String = "myEmail",
    var contacto: String = "myContact",
    var password: String = "myPassword",
    var student_number: String = "myStudentNumber"
) {
}
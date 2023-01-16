package pt.ipp.estg.doctorbrain.screens.sytemInputs

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Retorna um Objecto LocalDate a partir de uma String
 * @param date @[date] String to parse
 * @return parsed LocalDate
 * */
fun dateParse(date: String): LocalDate? {
    val dateToCompare: LocalDate? = try {
        LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (e: Exception) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy"))
        } catch (e: Exception) {
            try {
                LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/M/yyyy"))
            } catch (e: Exception) {
                LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yyyy"))
            }
        }
    }
    return dateToCompare
}
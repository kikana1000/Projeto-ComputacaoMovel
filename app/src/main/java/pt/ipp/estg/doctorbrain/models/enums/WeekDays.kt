package pt.ipp.estg.doctorbrain.models.enums

/**
 * Definição da enumeração de WeekDays
 */
enum class WeekDays {
    MONDAY, TUESDAY, WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY
}

fun getWeekDayByInt(week:Int): WeekDays {
    when(week){
        2 -> return WeekDays.MONDAY
        3 -> return WeekDays.TUESDAY
        4 -> return WeekDays.WEDNESDAY
        5 -> return WeekDays.THURSDAY
        6 -> return WeekDays.FRIDAY
        7 -> return WeekDays.SATURDAY
        else ->
            return WeekDays.SUNDAY
    }

}
fun StringToWeekDay(week:String): Int {
    when (week) {
        "SUNDAY"-> return 1
        "MONDAY" -> return 2
        "TUESDAY" -> return 3
        "WEDNESDAY" -> return 4
        "THURSDAY" -> return 5
        "FRIDAY" -> return 6
        "SATURDAY" -> return 7
        else ->
            return -1
    }
}
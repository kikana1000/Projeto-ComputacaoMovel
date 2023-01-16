package pt.ipp.estg.doctorbrain.screens.app.calendarScreen.Info

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import pt.ipp.estg.doctorbrain.models.HourDateSchoolClass
import pt.ipp.estg.doctorbrain.models.SchoolClass
import pt.ipp.estg.doctorbrain.models.SingleEvent
import pt.ipp.estg.doctorbrain.screens.app.calendarScreen.SchoolCard
import pt.ipp.estg.doctorbrain.screens.app.eventsScreen.SingleEventCard
import pt.ipp.estg.doctorbrain.screens.sytemInputs.dateParse
import java.time.LocalDate

/**
 * Gera uma lista de HourDateSchoolClass e SingleEvent ordenando por startHour e faz o display
 * de uma lista de SchoolClass e SingleEvent a acontecer numa data especifica
 *
 * para funcionar em condições as horas tem que estar guardadas em formato HH:mm,
 * se alguma estiver em H:m/HH:mm pode conter erros
 *
 * @param hours @param[hours] lista de HourDateSchoolClass a ordenar
 * @param classes @param[classes] lista usada para fazer o display das horas a cima apresentadas
 * @param singleEventList @param[singleEventList] lista de SingleEvents a ordenar
 * @param date @param[date] restrição de data
 */
@SuppressLint("SimpleDateFormat")
@Composable
fun ShowClassAndEventsSort(
    hours: List<HourDateSchoolClass>?,
    classes: List<SchoolClass>?,
    singleEventList: List<SingleEvent>?,
    date: LocalDate,
    moreinfoSchoolClass: (SchoolClass) -> Unit,
    moreinfoSingleEvent: (SingleEvent) -> Unit,
) {
    val list = ArrayList<Any>()
    if (hours != null) {
        list.addAll(hours)
    }
    if (singleEventList != null) {
        list.addAll(singleEventList)
    }

    list.sortedBy {
        if (it is HourDateSchoolClass) {
            it.startHour
        } else {
            it as SingleEvent
            it.startHour
        }
    }.forEach { it ->
        if (it is HourDateSchoolClass) {
            if (date.dayOfWeek.toString() == it.weekDays) {
                classes?.forEach { sclass ->
                    if (it.schooClass == sclass._eventID) {
                        SchoolCard(
                            sclass = sclass,
                            it.startHour,
                            it.finishtHour,
                            moreInfo = { moreinfoSchoolClass(sclass) })
                    }
                }
            }
        } else {
            it as SingleEvent
            if (date == dateParse(it.date)) {
                SingleEventCard(sEvent = (it), moreInfo = { moreinfoSingleEvent(it) }, true)
            }
        }
    }
}

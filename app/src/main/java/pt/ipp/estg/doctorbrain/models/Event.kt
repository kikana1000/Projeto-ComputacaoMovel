package pt.ipp.estg.doctorbrain.models

/**
 * Modelo de Dados de um Evento
 */
abstract class Event {
    abstract val _eventID: Int
    abstract val title: String
    abstract val local: String
}
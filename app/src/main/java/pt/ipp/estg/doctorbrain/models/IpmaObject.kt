package pt.ipp.estg.doctorbrain.models

data class WeatherData(
    var precipitaProb: String = "",
    var tMin: String = "",
    var tMax: String = "",
    var predWindDir: String = "",
    var idWeatherType: Int = -1,
    var classWindSpeed: Int = -1,
    var longitude: String = "",
    var forecastDate: String = "",
    var latitude: String = "",
    var classPrecInt: Int = -1
) {}


data class IpmaObject(
    var owner: String = "",
    var country: String = "",
    var data: List<WeatherData> = listOf(),
    var globalIdLocal: Int = -1,
    var dataUpdate: String = "",
)


data class WeatherTypeData(
    var descWeatherTypeEN: String = "",
    var descWeatherTypePT: String = "",
    var idWeatherType: Int = -1
)

data class WeatherTypeObject(
    var owner: String = "",
    var country: String = "",
    var data: List<WeatherTypeData> = listOf()
)

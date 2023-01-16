package pt.ipp.estg.doctorbrain.retrofitFun

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.ipp.estg.doctorbrain.models.IpmaObject
import pt.ipp.estg.doctorbrain.models.User
import pt.ipp.estg.doctorbrain.models.WeatherTypeObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


/**
 * Trata do retrofit para obter dados metereologicos
 */
class IPMA_ViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = "IPMA_ViewModel"
    val BASE_URL = "https://api.ipma.pt/"

    init {

    }


    interface ApiInterface {
        @GET("open-data/forecast/meteorology/cities/daily/1131200.json")//gets data of Porto District
        fun getWeather(): Call<IpmaObject>

        @GET("open-data/weather-type-classe.json")
        fun getWeatherType(): Call<WeatherTypeObject>
    }

    /**
     * Gets Weather Data relativa ao Porto do IPMA
     */
    fun getMyWeather(): MutableLiveData<IpmaObject>? {

        val result: MutableLiveData<IpmaObject>? = MutableLiveData<IpmaObject>()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getWeather() //vai buscar o metodo da ApiInterface

        retrofitData.enqueue(object : Callback<IpmaObject?> {
            override fun onResponse(call: Call<IpmaObject?>, response: Response<IpmaObject?>) {
                val responseBody = response.body()!!
                result!!.postValue(responseBody)
                Log.d("Response", responseBody.toString())
            }

            override fun onFailure(call: Call<IpmaObject?>, t: Throwable) {
                Log.d("MainActivity", "onFailure " + t.message)
                println("Se fodeu (Again)")
            }
        })
        return result
    }

    /**
     * Gets WeatherType Data para decifrar o idWeatherType
     */
    fun getMyWeatherType(): MutableLiveData<WeatherTypeObject>? {

        val result: MutableLiveData<WeatherTypeObject>? = MutableLiveData<WeatherTypeObject>()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getWeatherType() //vai buscar o metodo da ApiInterface

        retrofitData.enqueue(object : Callback<WeatherTypeObject?> {
            override fun onResponse(call: Call<WeatherTypeObject?>, response: Response<WeatherTypeObject?>) {
                val responseBody = response.body()!!
                result!!.postValue(responseBody)
                Log.d("Response", responseBody.toString())
            }

            override fun onFailure(call: Call<WeatherTypeObject?>, t: Throwable) {
                Log.d("MainActivity", "onFailure " + t.message)
                println("Se fodeu (Again)")
            }
        })
        return result
    }



}
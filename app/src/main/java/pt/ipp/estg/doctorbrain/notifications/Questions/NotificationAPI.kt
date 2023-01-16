package pt.ipp.estg.doctorbrain.notifications.Questions

import pt.ipp.estg.doctorbrain.models.QuestionNotification
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import okhttp3.ResponseBody
import retrofit2.Response

const val BASE_URL="https://fcm.googleapis.com/"
const val SERVER_KEY="AAAA_jo8kMU:APA91bGbOsfsUAuAIQscmxCdbkRJoTNo3uMdoWUbibN1GOtFf53I1xbNYuNDGZEMY3SJk8w5K_6aD30g_Pfiyq-Nl7spJfLBo-GwG5ZJ9-EMSdTcfTvQdiYIlJcLq796d_-xMTU6XOBL"
const val CONTENT_TYPE="application/json"
interface NotificationAPI {
    @Headers("Content-Type: $CONTENT_TYPE","Authorization: key=$SERVER_KEY")
    @POST("fcm/send")
    suspend fun postNotification(@Body data:QuestionNotification): Response<ResponseBody>
}
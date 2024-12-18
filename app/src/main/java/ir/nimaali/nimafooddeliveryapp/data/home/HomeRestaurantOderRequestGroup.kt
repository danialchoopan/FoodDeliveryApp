package ir.nimaali.nimafooddeliveryapp.data.home

import android.content.Context
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import ir.nimaali.nimafooddeliveryapp.data.RequestEndPoints
import ir.nimaali.nimafooddeliveryapp.data.VolleySingleTon
import ir.nimaali.nimafooddeliveryapp.models.home.HomePageRestaurant
import ir.nimaali.nimafooddeliveryapp.models.user.UserSellerRegisterLogin
import org.json.JSONObject

class HomeRestaurantOderRequestGroup(val m_context: Context) {

    val userSharedPreferences = m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    //home page restaurant
    fun homePageRestaurant (
        result: (success: Boolean,homePageRestaurant: HomePageRestaurant) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.POST, RequestEndPoints.userRestaurants,
            JSONObject(),
            { response ->
                try {

                    val homeResult =
                        Gson().fromJson(response.toString(), HomePageRestaurant::class.java)
                    val responseRequest = homeResult.success
                    if (responseRequest) {
                        result(true,homeResult)
                    } else {
                        result(false,homeResult)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val tokenUser = userSharedPreferences.getString("user_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }


    //show food res


    //add order

    //add comment

    //get comment

    //
}
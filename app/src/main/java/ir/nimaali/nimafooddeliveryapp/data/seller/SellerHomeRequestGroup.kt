package ir.nimaali.nimafooddeliveryapp.data.seller

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import ir.nimaali.nimafooddeliveryapp.data.RequestEndPoints
import ir.nimaali.nimafooddeliveryapp.data.VolleySingleTon
import ir.nimaali.nimafooddeliveryapp.models.seller.SellerFoodsListShow
import org.json.JSONObject

class SellerHomeRequestGroup(val m_context: Context) {

    val sellerSharedPreferences = m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)


    fun getSellerStatus(result_request: (status: Boolean) -> Unit) {
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.sellerOpenStatus, JSONObject(),
            Response.Listener { response ->
                result_request(response.getBoolean("seller_open"))
            },
            Response.ErrorListener { error ->
                println(error)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val tokenSeller = sellerSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenSeller"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonObjectRequest)
    }

    fun setSellerStatus(open:Boolean,result_request: (status: Boolean) -> Unit) {
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, RequestEndPoints.sellerOpen, JSONObject().apply {
                put("open",open)
            },
            Response.Listener { response ->
                result_request(response.getBoolean("seller_open"))
            },
            Response.ErrorListener { error ->
                println(error)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val tokenSeller = sellerSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenSeller"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonObjectRequest)
    }


    //get last 3 درانتظار تایید سفارش

}
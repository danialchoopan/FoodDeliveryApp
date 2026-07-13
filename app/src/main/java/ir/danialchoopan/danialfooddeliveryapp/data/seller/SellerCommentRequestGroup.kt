package ir.danialchoopan.danialfooddeliveryapp.data.seller

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import ir.danialchoopan.danialfooddeliveryapp.data.RequestEndPoints
import ir.danialchoopan.danialfooddeliveryapp.data.VolleySingleTon
import ir.danialchoopan.danialfooddeliveryapp.models.seller.SellerFoodsListShow
import ir.danialchoopan.danialfooddeliveryapp.models.seller.comment.SellerCommentShow
import org.json.JSONObject

class SellerCommentRequestGroup(val m_context: Context)  {

    val userSharedPreferences = m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    fun showCommentSeller(result_request: (success: Boolean, sellerCommentShow: SellerCommentShow) -> Unit){
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.sellerShowComment, JSONObject(),
            Response.Listener { response ->
                val comments =
                    Gson().fromJson(response.toString(), SellerCommentShow::class.java)
                result_request(true, comments)
            },
            Response.ErrorListener { error ->
                println(error)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val tokenSeller = userSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenSeller"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonObjectRequest)
    }

    fun DeleteCommentSeller(
        comment_id: String,
        result: (success: Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.DELETE, RequestEndPoints.sellerDeleteComment + "/" + comment_id,
            JSONObject(),
            { response ->
                try {
                    result(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->

                result(false)
                error.printStackTrace()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val tokenUser = userSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }
}
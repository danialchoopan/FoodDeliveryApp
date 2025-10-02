package ir.nimaali.nimafooddeliveryapp.data.seller

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.nimaali.nimafooddeliveryapp.data.RequestEndPoints
import ir.nimaali.nimafooddeliveryapp.data.VolleySingleTon
import ir.nimaali.nimafooddeliveryapp.models.seller.dash.SellerOrdersDashShowItem
import ir.nimaali.nimafooddeliveryapp.models.seller.dash.orderDetail.SellerOrderDetailByid
import ir.nimaali.nimafooddeliveryapp.models.user.order.UserOrderDetail
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

    fun setSellerStatus(open: Boolean, result_request: (status: Boolean) -> Unit) {
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, RequestEndPoints.sellerOpen, JSONObject().apply {
                put("open", open)
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

    fun getNotCompleteOrderSeller(
        result: (orderListUsersAll: List<SellerOrdersDashShowItem>) -> Unit,
    ) {
        val jsonRequest = object : JsonArrayRequest(
            Method.GET,
            RequestEndPoints.sellerShowOrderDash,
            null,
            { response ->
                try {
                    val orderList: List<SellerOrdersDashShowItem> = Gson().fromJson(
                        response.toString(),
                        object : TypeToken<List<SellerOrdersDashShowItem>>() {}.type
                    )
                    result(orderList)
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
                val tokenUser = sellerSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    fun getAllOrderSeller(
        result: (orderListUsersAll: List<SellerOrdersDashShowItem>) -> Unit,
    ) {
        val jsonRequest = object : JsonArrayRequest(
            Method.GET,
            RequestEndPoints.sellerShowOrderDashAll,
            null,
            { response ->
                try {
                    val orderList: List<SellerOrdersDashShowItem> = Gson().fromJson(
                        response.toString(),
                        object : TypeToken<List<SellerOrdersDashShowItem>>() {}.type
                    )
                    result(orderList)
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
                val tokenUser = sellerSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }


    fun setGettingOrderReady(
        order_id: String,
        result: (success: Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.PUT, RequestEndPoints.sellerOrderPreparing + "/" + order_id,
            JSONObject(),
            { response ->
                try {
                    result(true)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
                result(false)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val tokenUser = sellerSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    fun setCompletedOrderReady(
        order_id: String,
        result: (success: Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.PUT, RequestEndPoints.sellerOrderCompleted + "/" + order_id,
            JSONObject(),
            { response ->
                try {
                    result(true)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
                result(false)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val tokenUser = sellerSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    fun setCanselBySellerOrderReady(
        order_id: String,
        result: (success: Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.PUT, RequestEndPoints.sellerOrderCancel + "/" + order_id,
            JSONObject(),
            { response ->
                try {
                    result(true)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
                result(false)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val tokenUser = sellerSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    fun sellerGetOrderDetailById(
        order_id: String,
        result: (sellerOrderDetailByid: SellerOrderDetailByid) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.sellerShowOrderById + "/" + order_id,
            JSONObject(),
            { response ->
                try {
                    val orderResult =
                        Gson().fromJson(response.toString(), SellerOrderDetailByid::class.java)

                    result(orderResult)

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
                val tokenUser = sellerSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }


    fun sellerGetTotalOrder(result: (success:String,failed:String,totalOrder: String) -> Unit) {
        val jsonRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.sellerTotalOrder,
            JSONObject(),
            { response ->
                result(
                    response.getInt("completed_orders_count").toString(),
                    response.getInt("canceled_orders_count").toString(),
                    response.getInt("total_price").toString(),
                )
            },
            { error ->
                error.printStackTrace()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val tokenUser = sellerSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

}
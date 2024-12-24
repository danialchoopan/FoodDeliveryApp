package ir.nimaali.nimafooddeliveryapp.data.home

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.nimaali.nimafooddeliveryapp.data.RequestEndPoints
import ir.nimaali.nimafooddeliveryapp.data.VolleySingleTon
import ir.nimaali.nimafooddeliveryapp.models.home.HomePageRestaurant
import ir.nimaali.nimafooddeliveryapp.models.home.detail.Comment
import ir.nimaali.nimafooddeliveryapp.models.home.detail.Food
import ir.nimaali.nimafooddeliveryapp.models.home.detail.ShowRestaurantDetailUser
import ir.nimaali.nimafooddeliveryapp.models.home.order.OrderListUsersAll
import ir.nimaali.nimafooddeliveryapp.models.home.order.OrderListUsersAllItem
import ir.nimaali.nimafooddeliveryapp.models.user.UserSellerRegisterLogin
import ir.nimaali.nimafooddeliveryapp.models.user.order.UserOrderDetail
import org.json.JSONException
import org.json.JSONObject

class HomeRestaurantOderRequestGroup(val m_context: Context) {

    val userSharedPreferences = m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    //home page restaurant
    fun homePageRestaurant(
        result: (success: Boolean, homePageRestaurant: HomePageRestaurant) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.userRestaurants,
            JSONObject(),
            { response ->
                try {

                    val homeResult =
                        Gson().fromJson(response.toString(), HomePageRestaurant::class.java)
                    val responseRequest = homeResult.success
                    if (responseRequest) {
                        result(true, homeResult)
                    } else {
                        result(false, homeResult)
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

    //home page restaurant detail
    fun homePageRestaurantDetailById(
        restaurantId: String,
        result: (
            name: String,
            foods: List<Food>,
            comments: List<Comment>,
            category: String,
            address: String
        ) -> Unit
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.userRestaurantsDetail + "/" + restaurantId,
            JSONObject(),
            { response ->
                try {
                    // استخراج داده‌های JSON
                    val restaurants = response.getJSONObject("restaurants")

                    val name = restaurants.getString("restaurant_name")
                    val category = restaurants.getString("restaurant_category")
                    val address = restaurants.getString("restaurant_address")

                    // پردازش لیست غذاها
                    val foodsArray = restaurants.getJSONArray("foods")
                    val foods = mutableListOf<Food>()
                    for (i in 0 until foodsArray.length()) {
                        val foodObj = foodsArray.getJSONObject(i)
                        foods.add(
                            Food(
                                foodId = foodObj.getInt("food_id"),
                                foodName = foodObj.getString("food_name"),
                                foodPrice = foodObj.getString("food_price"),
                                foodAvailability = foodObj.getBoolean("food_availability"),
                                foodDescription = foodObj.getString("food_description"),
                                foodImage = foodObj.getString("food_image")
                            )
                        )
                    }

                    // پردازش لیست نظرات
                    val commentsArray = restaurants.getJSONArray("comments")
                    val comments = mutableListOf<Comment>()
                    for (i in 0 until commentsArray.length()) {
                        val commentObj = commentsArray.getJSONObject(i)
                        comments.add(
                            Comment(
                                commentId =  commentObj.getInt("comment_id"),
                                commentContent = commentObj.getString("comment_content"),
                                commentUserId = commentObj.getInt("comment_user_id"),
                                commentSellerId = commentObj.getInt("comment_seller_id")
                            )
                        )
                    }

                    // ارسال داده‌ها به صورت پارامترهای جداگانه
                    result(name, foods, comments, category, address)

                } catch (e: JSONException) {
                    e.printStackTrace()
                    // مدیریت خطای JSON
                }
            },
            { error ->
                error.printStackTrace()
                // مدیریت خطا
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



    fun AddCommentDetail(
        restaurant_id: String,
        content: String,
        result: (success: Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.POST, RequestEndPoints.userAddCommentRestaurantsDetail,
            JSONObject().apply {
                put("seller_id", restaurant_id)
                put("content", content)
            },
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
                val tokenUser = userSharedPreferences.getString("user_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    fun DeleteCommentDetail(
        comment_id: String,
        result: (success: Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.DELETE, RequestEndPoints.userDeleteCommentRestaurantsDetail + "/" + comment_id,
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
                val tokenUser = userSharedPreferences.getString("user_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    //place order
    fun placeOrder(
        sellerId: String,
        details: String,
        result: (success: Boolean) -> Unit
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.POST,
            RequestEndPoints.placeOrderUser,
            JSONObject().apply {
                put("seller_id", sellerId)
                put("details", details)
            },
            { response ->
                try {
                    result(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    result(false)
                }
            },
            { error ->
                result(false)
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



    fun getShowOrdersUser(
        result: (orderListUsersAll: List<OrderListUsersAllItem>) -> Unit
    ) {
        val jsonRequest = object : JsonArrayRequest(
            Method.GET,
            RequestEndPoints.usersOrdersShow,
            null,
            { response ->
                try {
                    // تبدیل JSONArray به لیستی از مدل‌ها
                    val orderList: List<OrderListUsersAllItem> = Gson().fromJson(
                        response.toString(),
                        object : TypeToken<List<OrderListUsersAllItem>>() {}.type
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
                val tokenUser = userSharedPreferences.getString("user_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    fun getAllOrdersUser(
        result: (orderListUsersAll: List<OrderListUsersAllItem>) -> Unit
    ) {
        val jsonRequest = object : JsonArrayRequest(
            Method.GET,
            RequestEndPoints.usersOrdersShowAll,
            null,
            { response ->
                try {
                    // تبدیل JSONArray به لیستی از مدل‌ها
                    val orderList: List<OrderListUsersAllItem> = Gson().fromJson(
                        response.toString(),
                        object : TypeToken<List<OrderListUsersAllItem>>() {}.type
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
                val tokenUser = userSharedPreferences.getString("user_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }


    fun getUserOrderDetailByID(order_id:String,
        result: (userOrderDetail: UserOrderDetail) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.usersOrdersShow+"/"+order_id,
            JSONObject(),
            { response ->
                try {

                    val orderResult =
                        Gson().fromJson(response.toString(), UserOrderDetail::class.java)

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
                val tokenUser = userSharedPreferences.getString("user_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    fun setOrderCancelByUser(order_id:String,
                               result: (success:Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.PUT, RequestEndPoints.usersOrderCancel+"/"+order_id,
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
                val tokenUser = userSharedPreferences.getString("user_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    fun getUserOrderData(result: (success:String,failed:String,totalOrder: String) -> Unit) {
        val jsonRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.showUserOrderData,
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
                val tokenUser = userSharedPreferences.getString("user_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    //add comment

    //get comment

    //
}
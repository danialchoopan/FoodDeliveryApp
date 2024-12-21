package ir.nimaali.nimafooddeliveryapp.data.seller


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import ir.nimaali.nimafooddeliveryapp.data.RequestEndPoints
import ir.nimaali.nimafooddeliveryapp.data.VolleySingleTon
import ir.nimaali.nimafooddeliveryapp.models.home.HomePageRestaurant
import ir.nimaali.nimafooddeliveryapp.models.user.UserSellerRegisterLogin
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import android.net.Uri

import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


import android.util.Base64
import ir.nimaali.nimafooddeliveryapp.models.seller.GetFoodForEdit
import ir.nimaali.nimafooddeliveryapp.models.seller.SellerFoodsListShow


class SellerFoodRequestGroup(val m_context: Context) {

    val userSharedPreferences = m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    fun convertImageToBase64(inputStream: InputStream): String {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun addSellerFood(
        name: String,
        description: String,
        price: String,
        imageUri: Uri?
    ) {
        // Convert the image URI to a Base64 string
        val base64Image = imageUri?.let { uri ->
            m_context.contentResolver.openInputStream(uri)?.let { inputStream ->
                convertImageToBase64(inputStream)
            }
        }
        // Send the request
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, RequestEndPoints.sellerFood, JSONObject().apply {
                put("name", name)
                put("description", description)
                put("price", price)
                put("photo", base64Image ?: "") // Empty string if no image is selected

            },
            Response.Listener { response ->

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

    fun showSellerFoods(result_request: (success: Boolean, sellerFoodsListShow: SellerFoodsListShow) -> Unit) {

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.sellerFood, JSONObject(),
            Response.Listener { response ->

                val foodResult =
                    Gson().fromJson(response.toString(), SellerFoodsListShow::class.java)
                result_request(true, foodResult)
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

    fun deleteSellerFoods(food_id:String,result_request: () -> Unit) {
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.DELETE, RequestEndPoints.sellerFood+"/"+food_id, JSONObject(),
            Response.Listener { response ->
                result_request()
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

    fun getSellerFoodById(food_id:String,result_request: (success: Boolean, foodForEdit: GetFoodForEdit) -> Unit) {
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.sellerFoodEditGet+"/"+food_id, JSONObject(),
            Response.Listener { response ->
                val foodResult = Gson().fromJson(response.toString(), GetFoodForEdit::class.java)
                result_request(true, foodResult)
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

    fun editSellerFood(food_id: String,
        name: String,
        description: String,
        price: String,
        imageUri: Uri?
    ) {
        // Convert the image URI to a Base64 string
        val base64Image = imageUri?.let { uri ->
            m_context.contentResolver.openInputStream(uri)?.let { inputStream ->
                convertImageToBase64(inputStream)
            }
        }
        // Send the request
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.PUT, RequestEndPoints.sellerFoodEditGet+"/"+food_id.toString(), JSONObject().apply {
                put("name", name)
                put("description", description)
                put("price", price)
                put("photo", base64Image ?: "") // Empty string if no image is selected

            },
            Response.Listener { response ->

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


}
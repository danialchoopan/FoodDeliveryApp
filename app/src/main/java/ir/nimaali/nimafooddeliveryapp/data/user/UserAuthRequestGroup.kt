package ir.nimaali.nimafooddeliveryapp.data.user

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import ir.nimaali.nimafooddeliveryapp.data.RequestEndPoints
import ir.nimaali.nimafooddeliveryapp.data.VolleySingleTon
import ir.nimaali.nimafooddeliveryapp.models.home.detail.Comment
import ir.nimaali.nimafooddeliveryapp.models.home.detail.Food
import ir.nimaali.nimafooddeliveryapp.models.user.UserSellerRegisterLogin
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream

class UserAuthRequestGroup(val m_context: Context) {

    val userSharedPreferences = m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)

    fun registerUser(
        name: String,
        phone: String,
        password: String,
        city_name: String,
        address: String,
        resultRequest: (success: Boolean, registerResponse: UserSellerRegisterLogin) -> Unit
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.POST,
            RequestEndPoints.userRegister,
            createJsonRequestUserRegister(
                name,
                phone,
                password,
                city_name,
                address
            ),  // Pass the JSON body here
            { response ->
                Log.d("register log", response.toString())
                try {
                    val registerResult =
                        Gson().fromJson(response.toString(), UserSellerRegisterLogin::class.java)
                    resultRequest(true, registerResult)

                    // Saving user data on phone
                    userSharedPreferences.edit().also {
                        it.putString("user_token", registerResult.token)
                        it.putString("has_login", "user")
                    }.apply()
                } catch (e: Exception) {
                    resultRequest(false, UserSellerRegisterLogin("", "", "", "", "",""))
                }
            },
            { error ->
                error.printStackTrace()
                resultRequest(false, UserSellerRegisterLogin("", "", "", "", "",""))
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }

    // Helper function to create a JSON object
    fun createJsonRequestUserRegister(
        name: String,
        phone: String,
        password: String,
        city_name: String,
        address: String
    ): JSONObject {
        val jsonParams = JSONObject()
        try {
            jsonParams.put("name", name)
            jsonParams.put("phone", phone)
            jsonParams.put("city_name", city_name)
            jsonParams.put("address", address)
            jsonParams.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonParams
    }


    fun loginUser(
        phone: String,
        password: String,
        resultRequest: (success: Boolean, loginResponse: UserSellerRegisterLogin) -> Unit
    ) {
        val jsonLoginRequest = object : JsonObjectRequest(
            Method.POST,
            RequestEndPoints.userLogin,
            JSONObject().apply {
                put("phone", phone)
                put("password", password)
            },
            { response ->
                try {
                    Log.d("login log", response.toString())
                    val loginResult =
                        Gson().fromJson(response.toString(), UserSellerRegisterLogin::class.java)
                    resultRequest(true, loginResult)

                    // Saving user data on the phone
                    userSharedPreferences.edit().also {
                        it.putString("user_token", loginResult.token)
                        it.putString("has_login", "user")
                        it.putString("user_name", loginResult.name)
                        it.putString("user_phone", phone)
                        it.putString("user_city", loginResult.cityName)
                        it.putString("user_id", loginResult.id)
                    }.apply()
                } catch (e: Exception) {
                    resultRequest(false, UserSellerRegisterLogin("", "", "", "", "",""))
                }
            },
            { error ->
                error.printStackTrace()
                resultRequest(false, UserSellerRegisterLogin("", "", "", "", "",""))
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                // Optionally add headers here
                return HashMap()
            }
        }

        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonLoginRequest)
    }

    //register seller
    fun registerSeller(
        restaurant_name: String,
        phone: String,
        password: String,
        city_name: String,
        category: String,
        address: String,
        resultRequest: (success: Boolean, registerResponse: UserSellerRegisterLogin) -> Unit
    ) {
        val jsonRegisterRequest = object : JsonObjectRequest(
            Method.POST,
            RequestEndPoints.sellerRegister,
            JSONObject().apply {
                put("restaurant_name", restaurant_name)
                put("city_name", city_name)
                put("category", category)
                put("phone", phone)
                put("address", address)
                put("password", password)
            },
            { response ->
                try {
                    Log.d("register log seller", response.toString())
                    val registerResult =
                        Gson().fromJson(response.toString(), UserSellerRegisterLogin::class.java)
                    resultRequest(true, registerResult)

                    // Saving user data on the phone
                    userSharedPreferences.edit().also {
                        it.putString("seller_token", registerResult.token)
                        it.putString("has_login", "seller")
                    }.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                    resultRequest(false, UserSellerRegisterLogin("", "", "", "", "",""))
                }
            },
            { error ->
                error.printStackTrace()
                resultRequest(false, UserSellerRegisterLogin("", "", "", "", "",""))
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                // Add any required headers, e.g., Content-Type
                return HashMap()
            }
        }

        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRegisterRequest)
    }

    //login request
    fun loginSeller(
        phone: String,
        password: String,
        resultRequest: (success: Boolean, loginResponse: UserSellerRegisterLogin) -> Unit
    ) {
        val jsonLoginRequest = object : JsonObjectRequest(
            Method.POST,
            RequestEndPoints.sellerLogin,
            JSONObject().apply {
                put("phone", phone)
                put("password", password)
            },
            { response ->
                try {
                    Log.d("login log seller", response.toString())
                    val loginResult =
                        Gson().fromJson(response.toString(), UserSellerRegisterLogin::class.java)
                    resultRequest(true, loginResult)

                    // Saving user data on the phone
                    userSharedPreferences.edit().also {
                        it.putString("seller_token", loginResult.token)
                        it.putString("has_login", "seller")
                        it.putString("seller_name", loginResult.name)
                        it.putString("seller_phone", phone)
                        it.putString("seller_city", loginResult.cityName)
                    }.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                    resultRequest(false, UserSellerRegisterLogin("", "", "", "", "",""))
                }
            },
            { error ->
                error.printStackTrace()
                resultRequest(false, UserSellerRegisterLogin("", "", "", "", "",""))
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                // Add optional headers if required (e.g., Content-Type)
                return HashMap()
            }
        }

        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonLoginRequest)
    }


    //edit user password
    fun userEditPasswordEdit(
        oldPassword: String,
        newPassword: String,
        result: (success: Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.POST, RequestEndPoints.userEditPassword,
            JSONObject().apply {
                put("old_password", oldPassword)
                put("new_password", newPassword)
            },
            { response ->
                try {
                    val responseRequest = response.getBoolean("success")
                    if (responseRequest) {
                        result(false)
                    } else {
                        result(true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    result(false)
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

    //edit user city name
    fun userEditCityName(
        newCityName: String,
        result: (success: Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.POST, RequestEndPoints.userEditCityName,
            JSONObject().apply {
                put("new_city_name", newCityName)
            },
            { response ->
                try {
                    val responseRequest = response.getBoolean("success")
                    if (responseRequest) {
                        result(true)
                        userSharedPreferences.edit().also {
                            it.putString("user_city", newCityName)
                        }.apply()
                    } else {
                        result(false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    result(false)
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


    //edit user city name
    fun sellerEditCityName(
        newCityName: String,
        result: (success: Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.POST, RequestEndPoints.sellerEditCityName,
            JSONObject().apply {
                put("new_city_name", newCityName)
            },
            { response ->
                try {
                    val responseRequest = response.getBoolean("success")
                    if (responseRequest) {
                        result(true)
                        userSharedPreferences.edit().also {
                            it.putString("seller_city", newCityName)
                        }.apply()
                    } else {
                        result(false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    result(false)
                }
            },
            { error ->
                error.printStackTrace()
                result(false)
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

    fun sellerEditPasswordEdit(
        oldPassword: String,
        newPassword: String,
        result: (success: Boolean) -> Unit,
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.POST, RequestEndPoints.sellerEditPassword,
            JSONObject().apply {
                put("old_password", oldPassword)
                put("new_password", newPassword)
            },
            { response ->
                try {
                    val responseRequest = response.getBoolean("success")
                    if (responseRequest) {
                        result(false)
                    } else {
                        result(true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    result(false)
                }
            },
            { error ->
                error.printStackTrace()
                result(false)
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
    fun convertImageToBase64(inputStream: InputStream): String {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun addBannerSeller(
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
            Method.PUT, RequestEndPoints.sellerUpdateBanner, JSONObject().apply {
                put("photo", base64Image ?: "")
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

    fun getSellerBanner(
        result: (img: String, ) -> Unit
    ) {
        val jsonRequest = object : JsonObjectRequest(
            Method.GET, RequestEndPoints.sellerGetBanner ,
            JSONObject(),
            { response ->
                try {
                    // استخراج داده‌های JSON
                    val img = response.getString("img")
                    result(img)

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
                val tokenUser = userSharedPreferences.getString("seller_token", "")
                headers["Authorization"] = "Bearer $tokenUser"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        VolleySingleTon.getInstance(m_context).addToRequestQueue(jsonRequest)
    }


}
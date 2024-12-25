package ir.nimaali.nimafooddeliveryapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import ir.nimaali.nimafooddeliveryapp.data.user.UserAuthRequestGroup
import ir.nimaali.nimafooddeliveryapp.models.user.UserData

class AuthUserSellerViewModel : ViewModel() {

    fun getUserLoginData(m_context: Context): UserData {
        val userSharedPreferences =
            m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
        val user = UserData(
            userSharedPreferences.getString("user_name", "").toString(),
            userSharedPreferences.getString("user_phone", "").toString(),
            userSharedPreferences.getString("user_address", "").toString(),
            userSharedPreferences.getString("user_city", "").toString(),
            userSharedPreferences.getString("user_token", "").toString(),
            userSharedPreferences.getString("user_id", "").toString(),
        )
        return user
    }

    fun setUserSellerHasLogin(m_context: Context,userLevel:String) {
        val userSharedPreferences =
            m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
        userSharedPreferences.edit().let {
            it.putString("has_login", userLevel)
        }.apply()
    }


    //user requests
    fun userLoginRequest(
        m_context: Context,
        phone: String,
        password: String,
        result: (success: Boolean) -> Unit
    ) {
        val userSharedPreferences =
            m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
        UserAuthRequestGroup(m_context)
            .loginUser(
                phone,
                password,
            ) { success, registerResponse ->

                result(success)
            }
    }
    //user requests
    fun userRegisterRequest(
        m_context: Context,
        name: String,
        phone: String,
        city_name: String,
        address: String,
        password: String,
        result: (success: Boolean) -> Unit
    ) {
        val userSharedPreferences =
            m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
        UserAuthRequestGroup(m_context)
            .registerUser(
                name,
                phone,
                password,
                city_name,
                address,
            ) { success, registerResponse ->
                if(success){
                    userSharedPreferences.edit().let {
                        it.putString("user_name",name)
                        it.putString("user_phone",phone)
                        it.putString("user_address",address)
                        it.putString("user_city",city_name)
                        it.putString("user_id",registerResponse.id)
                    }.apply()
                }
                result(success)
            }
    }

    //user requests
    fun sellerLoginRequest(
        m_context: Context,
        phone: String,
        password: String,
        result: (success: Boolean) -> Unit
    ) {
        val userSharedPreferences =
            m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
        UserAuthRequestGroup(m_context)
            .loginSeller(
                phone,
                password,
            ) { success, registerResponse ->
                result(success)
            }
    }
    //user requests
    fun sellerRegisterRequest(
        m_context: Context,
        restaurant_name: String,
        category: String,
        phone: String,
        city_name: String,
        address: String,
        password: String,
        result: (success: Boolean) -> Unit
    ) {
        val userSharedPreferences =
            m_context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
        UserAuthRequestGroup(m_context)
            .registerSeller(
                restaurant_name,
                phone,
                password,
                city_name,
                category,
                address,
            ) { success, registerResponse ->
                if(success){
                    userSharedPreferences.edit().let {
                        it.putString("seller_name",restaurant_name)
                        it.putString("seller_phone",phone)
                        it.putString("seller_city",city_name)
                        it.putString("seller_address",address)
                        it.putString("seller_category",city_name)
                        it.putString("seller_id",registerResponse.id)
                    }.apply()
                }
                result(success)
            }
    }

}
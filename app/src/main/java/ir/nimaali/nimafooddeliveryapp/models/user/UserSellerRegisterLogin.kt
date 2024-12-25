package ir.nimaali.nimafooddeliveryapp.models.user


import com.google.gson.annotations.SerializedName

data class UserSellerRegisterLogin(
    @SerializedName("city_name")
    val cityName: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("id")
    val id: String
)
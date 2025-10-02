package ir.nimaali.nimafooddeliveryapp.models.home.detail


import com.google.gson.annotations.SerializedName

data class ShowRestaurantDetailUser(
    @SerializedName("restaurants")
    val restaurants: Restaurants,
    @SerializedName("success")
    val success: Boolean
)

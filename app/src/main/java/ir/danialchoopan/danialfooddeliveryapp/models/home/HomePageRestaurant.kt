package ir.nimaali.nimafooddeliveryapp.models.home


import com.google.gson.annotations.SerializedName

data class HomePageRestaurant(
    @SerializedName("restaurants")
    val restaurants: List<Restaurant>,
    @SerializedName("success")
    val success: Boolean
)
package ir.nimaali.nimafooddeliveryapp.models.home.detail


import com.google.gson.annotations.SerializedName

data class Restaurants(
    @SerializedName("comments")
    val comments: List<Comment>,
    @SerializedName("foods")
    val foods: List<Food>,
    @SerializedName("restaurant_address")
    val restaurantAddress: String,
    @SerializedName("restaurant_category")
    val restaurantCategory: String,
    @SerializedName("restaurant_image")
    val restaurantImage: String,
    @SerializedName("restaurant_name")
    val restaurantName: String,
    @SerializedName("restaurant_open")
    val restaurantOpen: Boolean,
    @SerializedName("seller_id")
    val sellerId: Int
)
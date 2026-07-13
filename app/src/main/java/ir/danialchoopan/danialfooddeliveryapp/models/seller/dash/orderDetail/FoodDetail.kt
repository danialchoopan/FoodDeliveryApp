package ir.danialchoopan.danialfooddeliveryapp.models.seller.dash.orderDetail


import com.google.gson.annotations.SerializedName

data class FoodDetail(
    @SerializedName("food_img")
    val foodImg: String,
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("price")
    val price: String
)
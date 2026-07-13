package ir.danialchoopan.danialfooddeliveryapp.models.seller.dash


import com.google.gson.annotations.SerializedName

data class FoodDetail(
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("quantity")
    val quantity: Int
)
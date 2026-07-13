package ir.danialchoopan.danialfooddeliveryapp.models.seller


import com.google.gson.annotations.SerializedName

data class SellerFoodsListShow(
    @SerializedName("foods")
    val foods: List<Food>,
    @SerializedName("success")
    val success: String
)
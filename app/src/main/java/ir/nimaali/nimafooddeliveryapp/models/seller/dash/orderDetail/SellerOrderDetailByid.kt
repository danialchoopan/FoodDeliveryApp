package ir.nimaali.nimafooddeliveryapp.models.seller.dash.orderDetail


import com.google.gson.annotations.SerializedName

data class SellerOrderDetailByid(
    @SerializedName("food_details")
    val foodDetails: List<FoodDetail>,
    @SerializedName("order_date")
    val orderDate: String,
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("total_items")
    val totalItems: Int,
    @SerializedName("total_price")
    val totalPrice: Double,
    @SerializedName("user_address")
    val userAddress: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_name")
    val userName: String
)
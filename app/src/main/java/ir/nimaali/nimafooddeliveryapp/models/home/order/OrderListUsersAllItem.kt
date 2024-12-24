package ir.nimaali.nimafooddeliveryapp.models.home.order


import com.google.gson.annotations.SerializedName

data class OrderListUsersAllItem(
    @SerializedName("details")
    val details: List<Detail>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order_date")
    val orderDate: String,
    @SerializedName("seller_id")
    val sellerId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("seller_name")
    val seller_name: String,
    @SerializedName("total_price")
    val totalPrice: Double
)
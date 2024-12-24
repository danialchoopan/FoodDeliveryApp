package ir.nimaali.nimafooddeliveryapp.models.user.order


import com.google.gson.annotations.SerializedName

data class UserOrderDetail(
    @SerializedName("details")
    val details: List<Detail>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order_date")
    val orderDate: String,
    @SerializedName("seller_id")
    val seller_id: Int,
    @SerializedName("seller_name")
    val seller_name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("total_items")
    val total_items: Int,
    @SerializedName("total_price")
    val total_price: Double
)
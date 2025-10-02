package ir.nimaali.nimafooddeliveryapp.models.home.order


import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("food_id")
    val foodId: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("quantity")
    val quantity: Int
)
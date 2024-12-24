package ir.nimaali.nimafooddeliveryapp.models.user.order


import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("food_id")
    val foodId: Int,
    @SerializedName("food_img")
    val foodImg: String,
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("quantity")
    val quantity: Int
)
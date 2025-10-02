package ir.nimaali.nimafooddeliveryapp.models.home.detail


import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("food_availability")
    val foodAvailability: Boolean,
    @SerializedName("food_description")
    val foodDescription: String,
    @SerializedName("food_id")
    val foodId: Int,
    @SerializedName("food_image")
    val foodImage: String,
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("food_price")
    val foodPrice: String
)
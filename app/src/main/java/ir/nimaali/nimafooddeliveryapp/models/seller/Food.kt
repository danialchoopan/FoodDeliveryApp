package ir.nimaali.nimafooddeliveryapp.models.seller


import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("availability")
    val availability: Boolean,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("seller_id")
    val sellerId: Int
)
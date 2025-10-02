package ir.nimaali.nimafooddeliveryapp.models.seller


import com.google.gson.annotations.SerializedName

data class Foods(
    @SerializedName("availability")
    var availability: Boolean,
    @SerializedName("description")
    var description: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("photo")
    var photo: String,
    @SerializedName("price")
    var price: Int,
    @SerializedName("seller_id")
    var sellerId: Int
)
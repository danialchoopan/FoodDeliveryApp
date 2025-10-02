package ir.nimaali.nimafooddeliveryapp.models.home


import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("address")
    val address: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("open")
    val open: Boolean,
    @SerializedName("id")
    val id: Int
)
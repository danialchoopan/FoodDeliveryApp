package ir.nimaali.nimafooddeliveryapp.models.seller


import com.google.gson.annotations.SerializedName

data class GetFoodForEdit(
    @SerializedName("foods")
    val foods: Foods,
    @SerializedName("success")
    val success: String
)
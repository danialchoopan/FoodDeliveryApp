package ir.nimaali.nimafooddeliveryapp.models.seller.comment


import com.google.gson.annotations.SerializedName

data class SellerCommentShow(
    @SerializedName("comments")
    val comments: List<Comment>,
    @SerializedName("success")
    val success: Boolean
)
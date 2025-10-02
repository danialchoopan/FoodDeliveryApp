package ir.nimaali.nimafooddeliveryapp.models.seller.comment


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("comment_content")
    val commentContent: String,
    @SerializedName("comment_id")
    val commentId: Int,
    @SerializedName("comment_seller_id")
    val commentSellerId: Int,
    @SerializedName("comment_user_id")
    val commentUserId: Int
)
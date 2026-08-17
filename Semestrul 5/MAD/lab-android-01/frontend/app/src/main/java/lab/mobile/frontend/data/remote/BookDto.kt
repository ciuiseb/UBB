package lab.mobile.frontend.data.remote

import com.google.gson.annotations.SerializedName

data class BookDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("isNobelPrizeWinner") val isNobelPrizeWinner: Boolean,
    @SerializedName("publicationDate") val publicationDate: String
)
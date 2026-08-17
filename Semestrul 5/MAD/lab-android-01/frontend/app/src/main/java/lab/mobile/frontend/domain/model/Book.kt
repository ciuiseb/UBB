package lab.mobile.frontend.domain.model

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val isNobelPrizeWinner: Boolean,
    val publicationYear: String
)
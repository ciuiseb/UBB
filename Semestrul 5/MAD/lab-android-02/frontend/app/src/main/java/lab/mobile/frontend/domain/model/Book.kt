package lab.mobile.frontend.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: Int,
    val title: String,
    val author: String,
    val isNobelPrizeWinner: Boolean,
    val publicationDate: String
) {
    val publicationYear: String
        get() = publicationDate.take(4)
}
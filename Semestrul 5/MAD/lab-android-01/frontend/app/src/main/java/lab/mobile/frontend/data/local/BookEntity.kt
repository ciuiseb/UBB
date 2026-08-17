package lab.mobile.frontend.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val author: String,
    val isNobelPrizeWinner: Boolean,
    val publicationDate: String,
)
package lab.mobile.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Book (
    val id: Int = 0,
    val title: String,
    val author: String,
    val isNobelPrizeWinner: Boolean,
    val publicationDate: String
)

object Books : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val author = varchar("author", 128)
    val isNobelPrizeWinner = bool("is_nobel_prize_winner")
    val publicationDate = varchar("publication_date", 20)

    override val primaryKey = PrimaryKey(id)
}
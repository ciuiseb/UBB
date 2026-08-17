package lab.mobile.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User (
    val id: Int = 0,
    val username: String,
    val password: String,
    val profilePicture: String? = null
)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 128).uniqueIndex()
    val password = varchar("password", 128)
    val profilePicture = text("profile_picture").nullable()

    override val primaryKey = PrimaryKey(id)
}
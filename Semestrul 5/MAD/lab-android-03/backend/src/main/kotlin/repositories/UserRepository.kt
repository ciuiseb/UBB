package lab.mobile.repositories

import lab.mobile.model.User
import lab.mobile.model.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers

class UserRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun findUserByUsername(username: String): User? = dbQuery {
        Users.selectAll()
            .where { Users.username eq username }
            .map {
                User(
                    id = it[Users.id],
                    username = it[Users.username],
                    password = it[Users.password],
                    profilePicture = it[Users.profilePicture]
                )
            }
            .singleOrNull()
    }

    suspend fun updateProfilePicture(username: String, base64Image: String): Boolean = dbQuery {
        val updatedRows = Users.update({ Users.username eq username }) {
            it[profilePicture] = base64Image
        }
        updatedRows > 0
    }
}
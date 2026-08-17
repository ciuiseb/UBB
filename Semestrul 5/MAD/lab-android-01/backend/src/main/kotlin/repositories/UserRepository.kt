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
                    password = it[Users.password]
                )
            }
            .singleOrNull()
    }


    suspend fun saveUser(user: User): User? = dbQuery {
        val existing = Users.selectAll().where { Users.username eq user.username }.singleOrNull()
        if (existing != null) {
            return@dbQuery null
        }

        val id = Users.insert {
            it[username] = user.username
            it[password] = user.password
        } get Users.id

        User(id, user.username, user.password)
    }
}
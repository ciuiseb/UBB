package lab.mobile

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import lab.mobile.model.Books
import lab.mobile.model.Users

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:h2:file:./build/db/library;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )

    transaction(database) {
        SchemaUtils.create(Books, Users)
    }
}
package lab.mobile

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import lab.mobile.model.User
import lab.mobile.model.Book
import lab.mobile.repositories.BookRepository
import lab.mobile.repositories.UserRepository
import java.util.Date

fun Application.configureRouting() {
    val bookRepository = BookRepository()
    val userRepository = UserRepository()

    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtDomain = environment.config.property("jwt.domain").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()
    routing {
        post("/login") {
            val loginRequest = call.receive<User>()

            val user = userRepository.findUserByUsername(loginRequest.username)

            if (user != null && user.password == loginRequest.password) {
                val token = JWT.create()
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .withClaim("username", user.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                    .sign(Algorithm.HMAC256(jwtSecret))

                call.respond(mapOf("token" to token))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid username or password")
            }
        }
        authenticate("auth-jwt") {
            route("/user") {
                put("/photo") {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val request = call.receive<PhotoUploadDto>()

                    val success = userRepository.updateProfilePicture(username, request.image)
                    if (success) {
                        call.respond(HttpStatusCode.OK, "Photo updated")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Failed to update")
                    }
                }

                get("/me") {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val user = userRepository.findUserByUsername(username)

                    if (user != null) {
                        call.respond(user)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }

            route("/books") {
                //get all
                get {
                    val books = bookRepository.getAllBooks()
                    call.respond(books)
                }
                // get one
                get("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                        return@get
                    }

                    val book = bookRepository.getBookById(id)
                    if (book != null) {
                        call.respond(book)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Book not found")
                    }
                }

                //craete book
                post {
                    val book = call.receive<Book>()
                    val id = bookRepository.addBook(book)
                    call.respond(HttpStatusCode.Created, id)
                }

                // update book
                put("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                        return@put
                    }

                    val book = call.receive<Book>()
                    val updated = bookRepository.updateBook(id, book)

                    if (updated) {
                        call.respond(HttpStatusCode.OK, "Book updated")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Book not found")
                    }
                }

                //delete book
                delete("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                        return@delete
                    }

                    val deleted = bookRepository.deleteBook(id)
                    if (deleted) {
                        call.respond(HttpStatusCode.OK, "Book deleted")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Book not found")
                    }
                }
            }
        }
    }
}

@Serializable
data class PhotoUploadDto(val image: String)
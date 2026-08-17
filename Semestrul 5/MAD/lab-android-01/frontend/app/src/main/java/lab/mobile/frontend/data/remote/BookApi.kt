package lab.mobile.frontend.data.remote

import retrofit2.Response
import retrofit2.http.*

interface BookApi {

    @GET("/books")
    suspend fun getBooks(): List<BookDto>

    @GET("/books/{id}")
    suspend fun getBookById(@Path("id") id: Int): BookDto

    @POST("/books")
    suspend fun createBook(@Body book: BookDto): Int

    @PUT("/books/{id}")
    suspend fun updateBook(@Path("id") id: Int, @Body book: BookDto): Response<Unit>

    @DELETE("/books/{id}")
    suspend fun deleteBook(@Path("id") id: Int): Response<String>
}
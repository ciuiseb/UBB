package lab.mobile.frontend.data.remote

import kotlinx.serialization.Serializable
import lab.mobile.frontend.domain.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @GET("/user/me")
    suspend fun getUser(): User

    @PUT("/user/photo")
    suspend fun updatePhoto(@Body request: PhotoUploadDto)
}

@Serializable
data class PhotoUploadDto(
    val image: String
)
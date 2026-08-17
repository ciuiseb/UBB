package lab.mobile.frontend.data.remote

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class TokenResponse(
    @SerializedName("token") val token: String
)
package lab.mobile.frontend.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = 0,
    val username: String,
    val token: String? = null,
    val profilePicture: String? = null
)
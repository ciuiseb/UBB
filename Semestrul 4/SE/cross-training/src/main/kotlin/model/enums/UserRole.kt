package model.enums

enum class UserRole {
    USER, ADMIN, BANNED;

    override fun toString(): String = name.lowercase()

    companion object {
        fun fromString(value: String): UserRole =
            values().find { it.toString() == value.lowercase() } ?: USER
    }
}
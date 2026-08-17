package model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class FitnessLevel {
    NONE, BEGINNER, INTERMEDIATE, ADVANCED;

    override fun toString(): String = name.lowercase()

    companion object {
        fun fromString(value: String): FitnessLevel =
            values().find { it.toString() == value.lowercase() } ?: NONE
    }
}
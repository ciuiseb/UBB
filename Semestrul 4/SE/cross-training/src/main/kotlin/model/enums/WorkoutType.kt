package model.enums

enum class WorkoutType(val displayName: String) {
    EASY_RUN("Easy Run"),
    TEMPO_RUN("Tempo Run"),
    INTERVAL("Interval"),
    LONG_RUN("Long Run"),
    REST("Rest");

    override fun toString(): String = displayName
}
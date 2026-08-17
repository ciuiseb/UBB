package model.requests

data class FitnessLevelRequest(
    val userId: Long,
    val weeklyRunningDistance: String,
    val longestRecentRun: String,
    val runningExperience: String,
    val currentInjuries: String,
    val pace: String,

): APIRequest{
    companion object{
        val QUESTIONS = listOf(
            "How many kilometers (or miles) do you currently run per week, on average?",
            "What is the longest distance you've run in the past month, and how did it feel?",
            "How long have you been running regularly (e.g., at least 2–3 times per week)?",
            "Do you have any current injuries or recurring issues when you run?",
            "What is your average pace for a comfortable run (minutes per kilometer)?"
        )

    }
    override fun getRequestPrompt(): String {
        return """
    Based on the following responses to a running fitness assessment questionnaire, determine the runner's fitness level as either BEGINNER, INTERMEDIATE, or ADVANCED. Only return one of these three fitness levels, with no additional text or explanation.

    Fitness Assessment Responses:
    1. Weekly running distance: $weeklyRunningDistance
    2. Longest recent run and how it felt: $longestRecentRun
    3. Running experience (how long running regularly): $runningExperience
    4. Current injuries or recurring issues: $currentInjuries
    5. Usual pace: $pace

    Guidelines for classification:
    - BEGINNER: New to running, low weekly mileage (under 15km/10mi), no long runs over 5km, less than 6 months consistent running, may have injury concerns, basic goals like finishing a distance
    - INTERMEDIATE: Regular runner for 6+ months, weekly mileage of 15-40km, can complete 10km runs comfortably, some race experience, specific time goals
    - ADVANCED: Experienced runner (1+ years consistent training), 40+km, regular long runs of 15km+ with good recovery, minimal injury concerns, performance-oriented goals

    Based on this information, the runner's fitness level is:
    """
    }
}
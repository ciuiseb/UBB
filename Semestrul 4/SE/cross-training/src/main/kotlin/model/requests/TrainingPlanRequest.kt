package model.requests

import model.enums.FitnessLevel
import model.enums.WorkoutType

data class TrainingPlanRequest(
    val userId: Long,
    val targetDistance: String,
    val preparationWeeks: Int,
    val fitnessLevel: FitnessLevel,
    val trainingDaysPerWeek: Int,
): APIRequest {
    companion object {
        val QUESTIONS = listOf(
            "What is your target distance?",
            "How many weeks do you have to prepare?",
            "How many days do you want to train per week?"
        )
    }
    override fun getRequestPrompt(): String {
        val availableWorkouts = WorkoutType.values().joinToString(", ") { it.displayName }

        return """
            Create a $preparationWeeks-week training plan for a $fitnessLevel runner.
            
            Requirements:
            • Target distance: $targetDistance
            • Weeks to prepare: $preparationWeeks
            • Training days per week: $trainingDaysPerWeek
            • Available workout types: $availableWorkouts
            
            Return ONLY valid JSON in this exact format:
            
            {
              "trainingPlan": {
                "name": "descriptive plan name",
                "targetDistance": "$targetDistance",
                "preparationWeeks": $preparationWeeks
              },
              "trainingDays": [
                {
                  "dayNumber": 1,
                  "workoutType": "Easy Run",
                  "distance": "3km",
                  "duration": "25 minutes",
                  "description": "Comfortable pace to build base"
                }
              ]
            }
            
            Important:
            - workoutType must exactly match one of: $availableWorkouts
            - Include rest days in the schedule
            - Progress difficulty appropriately for $fitnessLevel level
            - No additional text, only JSON
        """.trimIndent()
    }
}
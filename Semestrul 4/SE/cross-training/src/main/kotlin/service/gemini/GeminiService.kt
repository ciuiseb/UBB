package service.gemini

import model.TrainingDay
import model.TrainingPlan
import model.enums.FitnessLevel
import model.requests.FitnessLevelRequest
import model.requests.TrainingPlanRequest

interface GeminiService{
    suspend fun determineFitnessLevel(request: FitnessLevelRequest): FitnessLevel
    suspend fun generateTrainingPlan(request: TrainingPlanRequest): Pair<TrainingPlan, List<TrainingDay>>
}
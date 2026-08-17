package service.gemini

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.*
import model.TrainingDay
import model.TrainingPlan
import model.enums.FitnessLevel
import model.enums.WorkoutType
import model.requests.*
import org.slf4j.LoggerFactory

data class GeminiServiceImpl(val config: GeminiConfig, val json: Json): GeminiService {

    private val logger = LoggerFactory.getLogger(GeminiServiceImpl::class.java)

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
            connectTimeoutMillis = 10_000
        }

    }

    override suspend fun determineFitnessLevel(request: FitnessLevelRequest): FitnessLevel {
        logger.info("Determining fitness level for user request")

        val requestPrompt = request.getRequestPrompt()

        val requestBody = buildJsonObject {
            put("contents", buildJsonArray {
                add(buildJsonObject {
                    put("parts", buildJsonArray {
                        add(buildJsonObject {
                            put("text", requestPrompt)
                        })
                    })
                })
            })
            put("generationConfig", buildJsonObject {
                put("temperature", 0.3)
                put("maxOutputTokens", 10)
            })
        }

        try {
            val response = httpClient.post("${config.baseUrl}/models/${config.model}:generateContent") {
                header("Content-Type", "application/json")
                header("x-goog-api-key", config.apiKey)
                setBody(requestBody.toString())
            }

            val responseText = response.body<String>()
            val jsonResponse = json.parseToJsonElement(responseText).jsonObject
            val candidates = jsonResponse["candidates"]?.jsonArray
            val content = candidates?.get(0)?.jsonObject?.get("content")?.jsonObject
            val parts = content?.get("parts")?.jsonArray
            val text = parts?.get(0)?.jsonObject?.get("text")?.jsonPrimitive?.content?.trim()?.uppercase()

            val fitnessLevel = try {
                FitnessLevel.valueOf(text ?: "NONE")
            } catch (e: IllegalArgumentException) {
                logger.warn("Invalid fitness level response '{}', defaulting to NONE", text)
                FitnessLevel.NONE
            }

            logger.info("Fitness level determined: {}", fitnessLevel)
            return fitnessLevel

        } catch (e: Exception) {
            logger.error("Failed to determine fitness level: {}", e.message)
            throw e
        }
    }

    override suspend fun generateTrainingPlan(request: TrainingPlanRequest): Pair<TrainingPlan, List<TrainingDay>> {
        logger.info("Generating training plan for user: {}", request.userId)

        val requestPrompt = request.getRequestPrompt()

        val requestBody = buildJsonObject {
            put("contents", buildJsonArray {
                add(buildJsonObject {
                    put("parts", buildJsonArray {
                        add(buildJsonObject {
                            put("text", requestPrompt)
                        })
                    })
                })
            })
            put("generationConfig", buildJsonObject {
                put("temperature", 0.7)
            })
        }

        try {
            val response = httpClient.post("${config.baseUrl}/models/${config.model}:generateContent") {
                header("Content-Type", "application/json")
                header("x-goog-api-key", config.apiKey)
                setBody(requestBody.toString())
            }

            val responseText = response.body<String>()
            val jsonResponse = json.parseToJsonElement(responseText).jsonObject

            if (jsonResponse.containsKey("error")) {
                val errorMessage = jsonResponse["error"]?.jsonObject?.get("message")?.jsonPrimitive?.content
                logger.error("Gemini API error: {}", errorMessage)
                throw IllegalStateException("Gemini API error: $errorMessage")
            }

            val candidates = jsonResponse["candidates"]?.jsonArray
            val content = candidates?.get(0)?.jsonObject?.get("content")?.jsonObject
            val parts = content?.get("parts")?.jsonArray
            val aiResponseText = parts?.get(0)?.jsonObject?.get("text")?.jsonPrimitive?.content

            if (aiResponseText.isNullOrBlank()) {
                logger.error("Empty response from Gemini API")
                throw IllegalStateException("Empty response from AI")
            }

            val cleanedJson = aiResponseText
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val aiJson = try {
                json.parseToJsonElement(cleanedJson).jsonObject
            } catch (e: Exception) {
                logger.error("Invalid JSON response from AI: {}", e.message)
                throw IllegalStateException("Invalid JSON response from AI", e)
            }

            val trainingPlanJson = aiJson["trainingPlan"]?.jsonObject
                ?: throw IllegalStateException("Missing trainingPlan in AI response")

            val trainingPlan = TrainingPlan(
                userId = request.userId,
                name = json.decodeFromJsonElement<String>(trainingPlanJson["name"]!!),
                targetDistance = json.decodeFromJsonElement<String>(trainingPlanJson["targetDistance"]!!),
                preparationWeeks = json.decodeFromJsonElement<Int>(trainingPlanJson["preparationWeeks"]!!)
            )

            val trainingDaysJson = aiJson["trainingDays"]?.jsonArray
                ?: throw IllegalStateException("Missing trainingDays in AI response")

            val trainingDays = trainingDaysJson.map { dayElement ->
                val dayObject = dayElement.jsonObject

                val workoutTypeString = json.decodeFromJsonElement<String>(dayObject["workoutType"]!!)
                val workoutTypeEnum = WorkoutType.values().find { it.displayName == workoutTypeString }
                    ?: run {
                        logger.warn("Unknown workout type '{}', using REST", workoutTypeString)
                        WorkoutType.REST
                    }

                TrainingDay(
                    trainingPlanId = 0,
                    dayNumber = json.decodeFromJsonElement<Int>(dayObject["dayNumber"]!!),
                    workoutType = workoutTypeEnum,
                    distance = dayObject["distance"]?.jsonPrimitive?.content,
                    duration = dayObject["duration"]?.jsonPrimitive?.content,
                    description = json.decodeFromJsonElement<String>(dayObject["description"]!!)
                )
            }

            logger.info("Training plan '{}' generated with {} days", trainingPlan.name, trainingDays.size)
            return Pair(trainingPlan, trainingDays)

        } catch (e: Exception) {
            logger.error("Failed to generate training plan for user {}: {}", request.userId, e.message)
            throw e
        }
    }
}
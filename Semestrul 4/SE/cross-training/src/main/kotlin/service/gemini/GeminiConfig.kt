package service.gemini

import java.io.FileNotFoundException
import java.util.*

data class GeminiConfig(
    val apiKey: String,
    val model: String,
    val baseUrl: String
) {
    companion object {
        fun load(): GeminiConfig {
            val properties = Properties()
            val configFile = Thread.currentThread().contextClassLoader
                .getResourceAsStream("api.properties")
                ?: throw FileNotFoundException("Could not find api.properties file in resources")

            properties.load(configFile)

            return GeminiConfig(
                apiKey = properties.getProperty("gemini.api.key")
                    ?: throw IllegalStateException("gemini.api.key not found in api.properties"),
                model = properties.getProperty("gemini.model")
                    ?: throw IllegalStateException("gemini.model not found in api.properties"),
                baseUrl = properties.getProperty("gemini.base.url")
                    ?: throw IllegalStateException("gemini.base.url not found in api.properties")
            )
        }
    }
}
package gui.navigation

import androidx.compose.runtime.*
import gui.*
import kotlinx.serialization.json.Json
import model.User
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import repository.impl.*
import service.gemini.*
import service.server.*

@Composable
fun RunningTrainingApp() {
    var currentScreen by remember { mutableStateOf(Screen.LOGIN) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    val service = remember { getService() }

    when (currentScreen) {
        Screen.LOGIN -> LoginScreen(
            service = service,
            onLoginSuccess = { user ->
                currentScreen = Screen.DASHBOARD
                currentUser = user
            },
            onNavigateToRegister = { currentScreen = Screen.REGISTER }
        )

        Screen.REGISTER -> RegistrationScreen(
            service = service,
            onNavigateToLogin = { currentScreen = Screen.LOGIN }
        )

        Screen.DASHBOARD -> currentUser?.let {
            DashboardScreen(
                user = it,
                service = service,
                onNavigateToFitnessForm = { currentScreen = Screen.FITNESS_FORM },
                onNavigateToTrainingPlanForm = { currentScreen = Screen.TRAINING_PLAN_FORM },
                onLogOut = {
                    currentScreen = Screen.LOGIN
                    currentUser = null
                }
            )
        }

        Screen.FITNESS_FORM -> currentUser?.let {
            FitnessLevelForm(
                user = it,
                service = service,
                onCancel = { currentScreen = Screen.DASHBOARD },
                onSubmit = { user ->
                    currentScreen = Screen.DASHBOARD
                    currentUser = user
                }
            )
        }

        Screen.TRAINING_PLAN_FORM -> currentUser?.let {
            TrainingPlanForm(
                user = it,
                service = service,
                onCancel = { currentScreen = Screen.DASHBOARD },
                onSubmit = { currentScreen = Screen.DASHBOARD }
            )
        }
    }
}

private fun getService(): Service {
    val sessionFactory = buildSessionFactory()
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = false
        coerceInputValues = true
    }
    val userRepo = UserRepositoryImpl(sessionFactory)
    val planRequestRepo = TrainingPlanRepositoryImpl(sessionFactory)
    val trainingDayRepo = TrainingDayRepositoryImpl(sessionFactory)
    val opeanaiService = GeminiServiceImpl(GeminiConfig.load(), json)
    return ServiceImpl(userRepo, planRequestRepo, trainingDayRepo, opeanaiService)
}

private fun buildSessionFactory(): SessionFactory {
    return Configuration()
        .configure("hibernate.cfg.xml")
        .buildSessionFactory()
}
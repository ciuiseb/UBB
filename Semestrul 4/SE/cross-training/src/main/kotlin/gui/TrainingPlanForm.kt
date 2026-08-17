package gui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.User
import model.requests.*
import service.server.Service

@Composable
fun TrainingPlanForm(
    user: User,
    service: Service,
    onCancel:() -> Unit,
    onSubmit: () -> Unit,
) {
    val answers = remember { mutableStateMapOf<Int, String>() }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Training Plan Creation",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        TrainingPlanRequest.QUESTIONS.forEachIndexed { index, question ->
            Text(
                text = question,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = answers[index] ?: "",
                onValueChange = { answers[index] = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onCancel() },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    val unansweredQuestions = TrainingPlanRequest.QUESTIONS.indices.filter {
                        answers[it].isNullOrBlank()
                    }

                    if (unansweredQuestions.isNotEmpty()) {
                        errorMessage = "Please answer all questions"
                        return@Button
                    } else {
                        val request = TrainingPlanRequest(
                            userId = user.id,
                            targetDistance = answers[0] ?: "",
                            preparationWeeks = answers[1]?.toIntOrNull() ?: 0,
                            fitnessLevel = user.fitnessLevel,
                            trainingDaysPerWeek = answers[2]?.toIntOrNull() ?: 0,
                        )
                        isLoading = true
                        coroutineScope.launch {
                            try {
                                val trainingPlan = service.generateTrainingPlan(request)
                                service.updateTrainingPlan(user, trainingPlan)
                                isLoading = false
                            } catch (e: Exception) {
                                isLoading = false
                                errorMessage = "Failed to submit training plan form: ${e.message}"
                            } finally {
                                onSubmit()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colors.onPrimary
                    )
                } else {
                    Text("Submit")
                }
            }
        }
    }
}
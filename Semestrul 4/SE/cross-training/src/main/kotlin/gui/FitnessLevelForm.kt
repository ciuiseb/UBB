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
import model.requests.FitnessLevelRequest
import service.server.Service

@Composable
fun FitnessLevelForm(
    user: User,
    service: Service,
    onCancel: () -> Unit,
    onSubmit: (User) -> Unit,
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
            text = "Running Fitness Assessment",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        FitnessLevelRequest.QUESTIONS.forEachIndexed { index, question ->
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
                    val unansweredQuestions = FitnessLevelRequest.QUESTIONS.indices.filter {
                        answers[it].isNullOrBlank()
                    }

                    if (unansweredQuestions.isNotEmpty()) {
                        errorMessage = "Please answer all questions"
                        return@Button
                    } else {
                        val request = FitnessLevelRequest(
                            userId = user.id,
                            weeklyRunningDistance = answers[0] ?: "",
                            longestRecentRun = answers[1] ?: "",
                            runningExperience = answers[2] ?: "",
                            currentInjuries = answers[3] ?: "",
                            pace = answers[4] ?: ""
                        )
                        isLoading = true
                        coroutineScope.launch {
                            try {
                                val fitnessLevel = service.assessFitnessLevel(request)
                                service.updateFitnessLevel(user, fitnessLevel)
                                isLoading = false
                                onSubmit(user.copy(fitnessLevel = fitnessLevel))
                            } catch (e: Exception) {
                                isLoading = false
                                errorMessage = "Failed to submit fitness level: ${e.message}"
                                onCancel()
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
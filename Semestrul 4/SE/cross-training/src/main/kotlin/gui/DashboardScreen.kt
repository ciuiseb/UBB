package gui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.TrainingDay
import model.TrainingPlan
import model.User
import model.enums.FitnessLevel
import service.server.Service

@Composable
fun DashboardScreen(
    user: User,
    service: Service,
    onNavigateToFitnessForm: () -> Unit,
    onNavigateToTrainingPlanForm: () -> Unit,
    onLogOut: () -> Unit
) {
    var trainingPlan by remember { mutableStateOf<TrainingPlan?>(null) }
    var todaysWorkout by remember { mutableStateOf<TrainingDay?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        if (user.fitnessLevel != FitnessLevel.NONE) {
            try {
                val plan = service.getUserTrainingPlan(user)
                trainingPlan = plan
            } catch (e: Exception) {
                error = "Error loading training plan: ${e.message}"
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    LaunchedEffect(trainingPlan) {
        trainingPlan?.let { plan ->
            todaysWorkout = service.getTodaysWorkout(plan)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Welcome, ${user.name}!",
                style = MaterialTheme.typography.h5
            )

            Button(onClick = { onLogOut() }) {
                Text("Logout")
            }
        }

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            when {
                user.fitnessLevel == FitnessLevel.NONE -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Your Fitness Level",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = "You need to set your fitness level before creating a training plan.",
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Button(
                                onClick = { onNavigateToFitnessForm() },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Assess Fitness Level")
                            }
                        }
                    }
                }

                trainingPlan == null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Your Training Plan",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = "Fitness Level: ${user.fitnessLevel}",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = "You don't have a training plan yet. Create one to start your journey!",
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Button(
                                onClick = {
                                    onNavigateToTrainingPlanForm()
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Create Training Plan")
                            }
                        }
                    }
                }

                else -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Today's Workout",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = "Fitness Level: ${user.fitnessLevel}",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            trainingPlan?.let { plan ->
                                Text(
                                    text = plan.name,
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            todaysWorkout?.let { workout ->
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                                Text(
                                    text = "Type: ${workout.workoutType}",
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "Distance: ${workout.distance ?: "N/A"}",
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "Duration: ${workout.duration ?: "N/A"}",
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "Description: ${workout.description}",
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            } ?: run {
                                Text(
                                    text = "No workout scheduled for today",
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Account Information",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Username: ${user.username}",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )

                    Text(
                        text = "Email: ${user.email}",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}
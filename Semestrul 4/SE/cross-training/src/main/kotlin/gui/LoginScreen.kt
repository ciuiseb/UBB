package gui
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.User
import service.server.Service

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    service: Service,
    onLoginSuccess: (User) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Running Training App", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(32.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.width(300.dp)
        )

        Spacer(Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.width(300.dp)
        )

        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val user = service.login(username, password)
                    if (user != null) {
                        onLoginSuccess(user)
                    } else {
                        error = "Invalid username or password"
                    }
                }
            },
            modifier = Modifier.width(300.dp)
        ) {
            Text("Login")
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = { onNavigateToRegister() }) {
            Text("Register")
        }
    }
}
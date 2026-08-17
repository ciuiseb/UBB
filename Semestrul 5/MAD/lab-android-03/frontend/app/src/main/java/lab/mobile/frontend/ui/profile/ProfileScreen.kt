package lab.mobile.frontend.ui.profile

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lab.mobile.frontend.utils.NetworkConnectivityObserver

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isOnline by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("User Name") }
    var isSaving by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val observer = NetworkConnectivityObserver(context)

        observer.isOnline.collect { status ->
            if (isOnline && !status) {
                snackbarHostState.showSnackbar(
                    message = "No connection, loading offline data...",
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
            }

            else if (!isOnline && status) {
                snackbarHostState.showSnackbar(
                    message = "Back online. Syncing data...",
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
            }
            isOnline = status
        }
    }

    fun handleSave() {
        scope.launch {
            isSaving = true
            delay(1500)
            isSaving = false

            if (isOnline) {
                snackbarHostState.showSnackbar("Profile updated on server!")
            } else {
                snackbarHostState.showSnackbar("Offline. Saved to local storage.")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isOnline) Color(0xFFE6FFFA) else Color(0xFFFFF5F5)
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff,
                        contentDescription = null,
                        tint = if (isOnline) Color(0xFF059669) else Color(0xFFE53E3E),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = if (isOnline) "Online" else "Offline",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isOnline) Color(0xFF059669) else Color(0xFFE53E3E)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Display Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { handleSave() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Changes")
                }
            }
        }
    }
}
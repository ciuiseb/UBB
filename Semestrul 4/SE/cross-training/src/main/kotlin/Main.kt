import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import gui.navigation.RunningTrainingApp

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Running Training App"
    ) {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                RunningTrainingApp()
            }
        }
    }
}

package lab.mobile.frontend.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.mobile.frontend.domain.repository.AuthRepository

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var loginError by mutableStateOf<String?>(null)
    var isLoginSuccessful by mutableStateOf(false)

    fun onLoginClick() {
        if (username.isBlank() || password.isBlank()) {
            loginError = "Please fill in all fields"
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                loginError = null

                repository.login(username, password)

                isLoginSuccessful = true
            } catch (e: Exception) {
                loginError = "Login failed: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}

package com.example.secureithelpdesk.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secureithelpdesk.data.AuthRepository
import com.example.secureithelpdesk.data.AppUser
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var errorMessage: String? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(false)
        private set

    var currentUser: AppUser? by mutableStateOf(null)
        private set

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                repository.register(email, password)

                currentUser = repository.getCurrentUserProfile()

                onSuccess()

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Registration failed"
            } finally {
                isLoading = false
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                repository.login(email, password)

                currentUser = repository.getCurrentUserProfile()

                if (currentUser == null) {
                    errorMessage = "User profile not found."
                    repository.logout()
                    return@launch
                }

                onSuccess()

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Login failed"
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        repository.logout()
        currentUser = null
    }
}

package com.example.secureithelpdesk.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secureithelpdesk.data.HelpdeskTicket
import com.example.secureithelpdesk.data.TicketRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TicketViewModel(
    private val repository: TicketRepo
) : ViewModel() {

    var isCreatingTicket by mutableStateOf(false)
        private set

    var createTicketError by mutableStateOf<String?>(null)
        private set

    fun getMyTickets(userId: String): Flow<List<HelpdeskTicket>> {
        return repository.getMyTickets(userId)
    }

    fun refreshMyTickets(
        userId: String,
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.refreshMyTickets(userId)
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Could not refresh tickets")
            }
        }
    }

    fun createTicket(
        ticket: HelpdeskTicket,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isCreatingTicket = true
            createTicketError = null

            try {
                repository.createTicket(ticket)
                onSuccess()
            } catch (e: Exception) {
                val error = e.localizedMessage ?: "Could not create ticket"
                createTicketError = error
                onError(error)
            } finally {
                isCreatingTicket = false
            }
        }
    }
}
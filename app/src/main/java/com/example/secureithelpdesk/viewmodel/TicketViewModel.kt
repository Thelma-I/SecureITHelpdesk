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

    fun refreshAllTickets(
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.refreshAllTickets()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Could not retrieve all tickets")
            }
        }
    }

    fun getAllTickets(): Flow<List<HelpdeskTicket>> {
        return repository.getAllTickets()
    }

    fun getAvailableTickets(): Flow<List<HelpdeskTicket>> {
        return repository.getAvailableTickets()
    }

    fun getAssignedTickets(technicianId: String): Flow<List<HelpdeskTicket>> {
        return repository.getAssignedTickets(technicianId)
    }
    fun updateTicketStatus(
        ticketId: String,
        status: String,
        resolutionNotes: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.updateTicketStatus(
                    ticketId = ticketId,
                    status = status,
                    resolutionNotes = resolutionNotes
                )

                onSuccess()

            } catch (e: Exception) {
                onError(
                    e.localizedMessage ?: "Could not update ticket"
                )
            }
        }
    }

    fun updateTechnicianTicketStatus(
        ticketId: String,
        status: String,
        resolutionNotes: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.updateTechnicianTicketStatus(
                    ticketId = ticketId,
                    status = status,
                    resolutionNotes = resolutionNotes
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Could not update ticket")
            }
        }
    }

    fun assignTicket(
        ticketId: String,
        technicianId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.assignTicket(
                    ticketId = ticketId,
                    technicianId = technicianId
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Could not assign ticket")
            }
        }
    }

    fun acceptTicket(
        ticketId: String,
        technicianId: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.acceptTicket(
                    ticketId = ticketId,
                    technicianId = technicianId
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Could not accept ticket")
            }
        }
    }

}
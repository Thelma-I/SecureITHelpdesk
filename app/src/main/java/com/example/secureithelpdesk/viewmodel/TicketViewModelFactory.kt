package com.example.secureithelpdesk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.secureithelpdesk.data.TicketRepo

class TicketViewModelFactory(
    private val repository: TicketRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(TicketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TicketViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

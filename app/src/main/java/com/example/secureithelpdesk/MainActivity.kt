package com.example.secureithelpdesk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.secureithelpdesk.data.HelpdeskDatabase
import com.example.secureithelpdesk.data.RoomTicketRepo
import com.example.secureithelpdesk.data.TicketRepo
import com.example.secureithelpdesk.navigation.AppNavigation
import com.example.secureithelpdesk.viewmodel.TicketViewModel
import com.example.secureithelpdesk.viewmodel.TicketViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = HelpdeskDatabase.getDatabase(applicationContext)
        val ticketDao = database.ticketDao()
        val roomTicketRepo = RoomTicketRepo(ticketDao)
        val ticketRepository = TicketRepo(localRepository = roomTicketRepo)
        setContent {

            val ticketViewModel: TicketViewModel = viewModel(
                factory = TicketViewModelFactory(ticketRepository)
            )
            AppNavigation(
                ticketViewModel = ticketViewModel
            )
        }
    }
}

package com.example.secureithelpdesk.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.secureithelpdesk.data.HelpdeskTicket

@Composable
fun AvailableTicketsScreen(
    tickets: List<HelpdeskTicket>,
    onAcceptTicket: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Available Tickets",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (tickets.isEmpty()) {
            Text("No available tickets at the moment.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tickets) { ticket ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = ticket.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("Category: ${ticket.category}")
                            Text("Description: ${ticket.description}")
                            Text("Status: ${ticket.status}")
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    onAcceptTicket(ticket.id)
                                }
                            ) {
                                Text("Accept Ticket")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = onBack) {
            Text("Back")
        }
    }
}

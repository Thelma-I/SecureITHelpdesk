
package com.example.secureithelpdesk.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.secureithelpdesk.data.HelpdeskTicket

@Composable
fun AdminDashboardScreen(
    tickets: List<HelpdeskTicket>,
    onUpdateStatus: (String, String, String) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Admin Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Manage and monitor support requests.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total Tickets: ${tickets.size}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (tickets.isEmpty()) {
            Text("No support tickets available.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = tickets,
                    key = { it.id }
                ) { ticket ->

                    var notes by remember(ticket.id) {
                        mutableStateOf(ticket.resolutionNotes)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = ticket.title,
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Ticket ID: ${ticket.id}")
                            Text("Submitted by: ${ticket.userId}")
                            Text("Category: ${ticket.category}")

                            Text(
                                text = "Status: ${ticket.status}",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(ticket.description)

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = notes,
                                onValueChange = { notes = it },
                                label = {
                                    Text("Resolution notes")
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    onUpdateStatus(
                                        ticket.id,
                                        "In Progress",
                                        notes
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Mark In Progress")
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = {
                                    onUpdateStatus(
                                        ticket.id,
                                        "Resolved",
                                        notes
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Mark Resolved")
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            TextButton(
                                onClick = {
                                    onUpdateStatus(
                                        ticket.id,
                                        "Open",
                                        notes
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Reopen Ticket")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Out")
        }
    }
}

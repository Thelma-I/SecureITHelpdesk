package com.example.secureithelpdesk.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.secureithelpdesk.data.HelpdeskTicket

@Composable
fun AssignedTicketsScreen(
    tickets: List<HelpdeskTicket>,
    onUpdateStatus: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Assigned Tickets",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (tickets.isEmpty()) {
            Text("You have no assigned tickets yet.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tickets, key = { it.id }) { ticket ->

                    var resolutionNotes by remember(ticket.id) {
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
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Category: ${ticket.category}")
                            Text("Description: ${ticket.description}")
                            Text("Status: ${ticket.status}")

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = resolutionNotes,
                                onValueChange = {
                                    resolutionNotes = it
                                },
                                label = {
                                    Text("Resolution Notes")
                                },
                                placeholder = {
                                    Text("Describe how you resolved the issue")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (ticket.status != "Resolved") {

                                OutlinedButton(
                                    onClick = {
                                        onUpdateStatus(
                                            ticket.id,
                                            "In Progress",
                                            resolutionNotes
                                        )
                                    }
                                ) {
                                    Text("Mark In Progress")
                                }

                                Button(
                                    onClick = {
                                        onUpdateStatus(
                                            ticket.id,
                                            "Resolved",
                                            resolutionNotes
                                        )
                                    },
                                    enabled = resolutionNotes.isNotBlank()
                                ) {
                                    Text("Mark Resolved")
                                }
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

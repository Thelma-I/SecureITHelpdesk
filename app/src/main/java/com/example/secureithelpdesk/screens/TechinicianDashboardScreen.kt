package com.example.secureithelpdesk.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TechnicianDashboardScreen(
    onViewAvailableTickets: () -> Unit,
    onViewAssignedTickets: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Technician Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Manage and resolve support tickets.")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onViewAvailableTickets,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Available Tickets")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onViewAssignedTickets,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("My Assigned Tickets")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log Out")
        }
    }
}

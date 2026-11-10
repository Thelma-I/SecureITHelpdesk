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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onCreateTicket: () -> Unit,
    onViewTickets: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "IT Helpdesk",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "How can we help you today?"
        )
        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = onCreateTicket,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Support Ticket")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onViewTickets,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("My Tickets")
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

@Preview
@Composable
fun homescreenpreview(){
    HomeScreen(onCreateTicket = {}, onLogout = {}, onViewTickets = {})
}

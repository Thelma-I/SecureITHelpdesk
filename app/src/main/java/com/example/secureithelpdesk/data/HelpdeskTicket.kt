package com.example.secureithelpdesk.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "helpdesk_tickets")
data class HelpdeskTicket(
    @PrimaryKey
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val status: String = "Open",
    val resolutionNotes: String = "",
    val createdAt: Long = 0L
)

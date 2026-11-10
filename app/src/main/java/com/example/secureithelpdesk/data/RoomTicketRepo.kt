package com.example.secureithelpdesk.data

import kotlinx.coroutines.flow.Flow

class RoomTicketRepo(
    private val ticketDao: TicketDao
) {
    suspend fun saveTicket(ticket: HelpdeskTicket) {
        ticketDao.insertTicket(ticket)
    }
    suspend fun saveTickets(tickets: List<HelpdeskTicket>) {
        ticketDao.insertTickets(tickets)
    }

    fun getMyTickets(userId: String): Flow<List<HelpdeskTicket>> {
        return ticketDao.getMyTickets(userId)
    }
    fun getAllTickets(): Flow<List<HelpdeskTicket>> {
        return ticketDao.getAllTickets()
    }

    fun getAvailableTickets(): Flow<List<HelpdeskTicket>> {
        return ticketDao.getAvailableTickets()
    }

    fun getAssignedTickets(technicianId: String): Flow<List<HelpdeskTicket>> {
        return ticketDao.getAssignedTickets(technicianId)
    }

    suspend fun updateTicket(ticket: HelpdeskTicket) {
        ticketDao.updateTicket(ticket)
    }
    suspend fun deleteTicket(ticketId: String) {
        ticketDao.deleteTicket(ticketId)
    }
}
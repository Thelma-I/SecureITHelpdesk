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

    suspend fun updateTicket(ticket: HelpdeskTicket) {
        ticketDao.updateTicket(ticket)
    }

    suspend fun deleteTicket(ticketId: String) {
        ticketDao.deleteTicket(ticketId)
    }
}

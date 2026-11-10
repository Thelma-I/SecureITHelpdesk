package com.example.secureithelpdesk.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: HelpdeskTicket)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTickets(tickets: List<HelpdeskTicket>)

    @Query("SELECT * FROM helpdesk_tickets WHERE userId = :userId ORDER BY createdAt DESC")
    fun getMyTickets(userId: String): Flow<List<HelpdeskTicket>>

    @Query("SELECT * FROM helpdesk_tickets ORDER BY createdAt DESC")
    fun getAllTickets(): Flow<List<HelpdeskTicket>>

    @Query("""
    SELECT * FROM helpdesk_tickets
    WHERE assignedTechnicianId IS NULL
    AND status = 'Open'
    ORDER BY createdAt DESC
""")
    fun getAvailableTickets(): Flow<List<HelpdeskTicket>>

    @Query("""
    SELECT * FROM helpdesk_tickets
    WHERE assignedTechnicianId = :technicianId
    ORDER BY createdAt DESC
""")
    fun getAssignedTickets(
        technicianId: String
    ): Flow<List<HelpdeskTicket>>
    @Update
    suspend fun updateTicket(ticket: HelpdeskTicket)

    @Query("DELETE FROM helpdesk_tickets WHERE id = :ticketId")
    suspend fun deleteTicket(ticketId: String)
}

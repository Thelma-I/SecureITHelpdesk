
package com.example.secureithelpdesk.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class TicketRepo(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val localRepository: RoomTicketRepo
) {
    suspend fun createTicket(ticket: HelpdeskTicket) {
        val document = firestore.collection("tickets").document()

        val newTicket = ticket.copy(
            id = document.id,
            createdAt = System.currentTimeMillis()
        )

        document.set(newTicket).await()
        localRepository.saveTicket(newTicket)
    }

    fun getMyTickets(userId: String): Flow<List<HelpdeskTicket>> {
        return localRepository.getMyTickets(userId)
    }

    suspend fun refreshMyTickets(userId: String) {
        val snapshot = firestore.collection("tickets")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        val tickets = snapshot.toObjects(HelpdeskTicket::class.java)

        localRepository.saveTickets(tickets)
    }
}

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

    suspend fun refreshAllTickets() {
        val snapshot = firestore.collection("tickets")
            .get()
            .await()

        val tickets = snapshot.toObjects(HelpdeskTicket::class.java)

        localRepository.saveTickets(tickets)
    }

    fun getAllTickets(): Flow<List<HelpdeskTicket>> {
        return localRepository.getAllTickets()
    }

    fun getAvailableTickets(): Flow<List<HelpdeskTicket>> {
        return localRepository.getAvailableTickets()
    }

    fun getAssignedTickets(technicianId: String): Flow<List<HelpdeskTicket>> {
        return localRepository.getAssignedTickets(technicianId)
    }
    suspend fun updateTicketStatus(
        ticketId: String,
        status: String,
        resolutionNotes: String
    ) {
        val document = firestore.collection("tickets")
            .document(ticketId)

        // Update Firestore first.
        document.update(
            mapOf(
                "status" to status,
                "resolutionNotes" to resolutionNotes
            )
        ).await()

        // Retrieve the updated ticket.
        val updatedTicket = document.get()
            .await()
            .toObject(HelpdeskTicket::class.java)
            ?: throw Exception("Updated ticket could not be retrieved.")

        // Update the local Room cache.
        localRepository.saveTicket(updatedTicket)
    }


    suspend fun updateTechnicianTicketStatus(
        ticketId: String,
        status: String,
        resolutionNotes: String
    ) {
        val document = firestore.collection("tickets").document(ticketId)

        val updates = mutableMapOf<String, Any>(
            "status" to status,
            "resolutionNotes" to resolutionNotes
        )

        if (status == "Resolved") {
            updates["completedAt"] = System.currentTimeMillis()
        }

        document.update(updates).await()

        val updatedTicket = document.get()
            .await()
            .toObject(HelpdeskTicket::class.java)
            ?: throw Exception("Updated ticket could not be retrieved.")

        localRepository.saveTicket(updatedTicket)
    }


    suspend fun assignTicket(
        ticketId: String,
        technicianId: String
    ) {
        // Retrieve the technician's profile.
        val technicianSnapshot = firestore.collection("users")
            .document(technicianId)
            .get()
            .await()

        val technician = technicianSnapshot.toObject(AppUser::class.java)
            ?: throw Exception("Technician profile not found.")

        // Verify that the selected account is a technician.
        if (technician.role != "TECHNICIAN") {
            throw Exception("Selected user is not a technician.")
        }

        // Assign the ticket in Firestore.
        val document = firestore.collection("tickets")
            .document(ticketId)

        document.update(
            mapOf(
                "assignedTechnicianId" to technician.uid,
                "assignedTechnicianName" to technician.email
            )
        ).await()

        // Retrieve and update the local Room cache.
        val updatedTicket = document.get()
            .await()
            .toObject(HelpdeskTicket::class.java)
            ?: throw Exception("Updated ticket could not be retrieved.")

        localRepository.saveTicket(updatedTicket)
    }


    suspend fun acceptTicket(
        ticketId: String,
        technicianId: String
    ) {
        // Retrieve the technician's profile.
        val technicianSnapshot = firestore.collection("users")
            .document(technicianId)
            .get()
            .await()

        val technician = technicianSnapshot.toObject(AppUser::class.java)
            ?: throw Exception("Technician profile not found.")

        if (technician.role != "TECHNICIAN") {
            throw Exception("Only technicians can accept tickets.")
        }

        // Retrieve the ticket.
        val document = firestore.collection("tickets")
            .document(ticketId)

        val ticketSnapshot = document.get().await()

        val ticket = ticketSnapshot.toObject(HelpdeskTicket::class.java)
            ?: throw Exception("Ticket not found.")

        // Prevent accepting an already assigned ticket.
        if (ticket.assignedTechnicianId != null) {
            throw Exception("This ticket has already been assigned.")
        }

        if (ticket.status != "Open") {
            throw Exception("Only open tickets can be accepted.")
        }

        // Accept the ticket.
        document.update(
            mapOf(
                "assignedTechnicianId" to technician.uid,
                "assignedTechnicianName" to technician.email,
                "acceptedAt" to System.currentTimeMillis(),
                "status" to "In Progress"
            )
        ).await()

        // Update the local Room cache.
        val updatedTicket = document.get()
            .await()
            .toObject(HelpdeskTicket::class.java)
            ?: throw Exception("Updated ticket could not be retrieved.")

        localRepository.saveTicket(updatedTicket)
    }
}

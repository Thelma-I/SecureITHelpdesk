package com.example.secureithelpdesk.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()
) {

    private val usersCollection = firestore.collection("users")

    suspend fun createUser(user: AppUser) {
        usersCollection
            .document(user.uid)
            .set(user)
            .await()
    }

    suspend fun getUser(uid: String): AppUser? {
        return usersCollection
            .document(uid)
            .get()
            .await()
            .toObject(AppUser::class.java)
    }
}


package com.example.secureithelpdesk.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val userRepository: UserRepository = UserRepository()
) {

    suspend fun register(
        email: String,
        password: String
    ) {
        val result = auth.createUserWithEmailAndPassword(
            email,
            password
        ).await()

        val firebaseUser = result.user
            ?: throw Exception("Account creation failed.")

        val appUser = AppUser(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: email,
            role = "USER"
        )

        userRepository.createUser(appUser)
    }

    suspend fun login(
        email: String,
        password: String
    ) {
        auth.signInWithEmailAndPassword(
            email,
            password
        ).await()
    }

    suspend fun getCurrentUserProfile(): AppUser? {
        val uid = auth.currentUser?.uid ?: return null

        return userRepository.getUser(uid)
    }

    fun logout() {
        auth.signOut()
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}

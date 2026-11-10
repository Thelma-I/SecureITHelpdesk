package com.example.secureithelpdesk.data

data class AppUser(
    val uid: String = "",
    val email: String = "",
    val role: String = "USER",
    val createdAt: Long = System.currentTimeMillis()
)

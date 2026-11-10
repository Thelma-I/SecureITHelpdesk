package com.example.secureithelpdesk.navigation


object AppRoutes {

    // Authentication
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Regular user
    const val HOME = "home"
    const val CREATE_TICKET = "create_ticket"
    const val MY_TICKETS = "my_tickets"

    // Technician
    const val TECHNICIAN_DASHBOARD = "technician_dashboard"
    const val AVAILABLE_TICKETS = "available_tickets"
    const val ASSIGNED_TICKETS = "assigned_tickets"

    // Administrator
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ALL_TICKETS = "all_tickets"
    const val WORK_HISTORY = "work_history"
    const val REPORTS = "reports"
    const val TECHNICIAN_MANAGEMENT = "technician_management"
}

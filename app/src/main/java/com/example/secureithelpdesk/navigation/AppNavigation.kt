package com.example.secureithelpdesk.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.secureithelpdesk.data.HelpdeskTicket
import com.example.secureithelpdesk.screens.AdminDashboardScreen
import com.example.secureithelpdesk.screens.AssignedTicketsScreen
import com.example.secureithelpdesk.screens.AvailableTicketsScreen
import com.example.secureithelpdesk.screens.auth.LoginScreen
import com.example.secureithelpdesk.screens.auth.RegisterScreen
import com.example.secureithelpdesk.screens.CreateTicketScreen
import com.example.secureithelpdesk.screens.HomeScreen
import com.example.secureithelpdesk.screens.MyTicketsScreen
import com.example.secureithelpdesk.screens.TechnicianDashboardScreen
import com.example.secureithelpdesk.viewmodel.AuthViewModel
import com.example.secureithelpdesk.viewmodel.TicketViewModel

@Composable
fun AppNavigation(
    ticketViewModel: TicketViewModel
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN
    ) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLogin = { email, password ->
                    authViewModel.login(
                        email = email,
                        password = password,
                        onSuccess = {
                            val destination = when (authViewModel.currentUser?.role) {
                                "ADMIN" -> AppRoutes.ADMIN_DASHBOARD
                                "TECHNICIAN" -> AppRoutes.TECHNICIAN_DASHBOARD
                                "USER" -> AppRoutes.HOME
                                else -> null
                            }

                            destination?.let {
                                navController.navigate(it) {
                                    popUpTo(AppRoutes.LOGIN) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    )
                },
                onRegisterClick = {
                    navController.navigate(AppRoutes.REGISTER)
                },
                isLoading = authViewModel.isLoading,
                errorMessage = authViewModel.errorMessage
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onRegister = { email, password ->
                    authViewModel.register(
                        email = email,
                        password = password,
                        onSuccess = {
                            navController.navigate(AppRoutes.HOME) {
                                popUpTo(AppRoutes.LOGIN) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                },
                onLoginClick = {
                    navController.popBackStack()
                },
                isLoading = authViewModel.isLoading,
                errorMessage = authViewModel.errorMessage
            )
        }

        composable(AppRoutes.HOME) {
            HomeScreen(
                onCreateTicket = {
                    navController.navigate(AppRoutes.CREATE_TICKET)
                },
                onViewTickets = {
                    navController.navigate(AppRoutes.MY_TICKETS)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppRoutes.CREATE_TICKET) {
            CreateTicketScreen(
                onSubmit = { title, description, category ->
                    val userId = authViewModel.currentUser?.uid

                    if (userId != null) {
                        val ticket = HelpdeskTicket(
                            userId = userId,
                            title = title,
                            description = description,
                            category = category
                        )

                        ticketViewModel.createTicket(
                            ticket = ticket,
                            onSuccess = {
                                navController.popBackStack()
                            },
                            onError = {
                                // The ViewModel already stores the error.
                            }
                        )
                    }
                },
                onBack = {
                    navController.popBackStack()
                },
                isLoading = ticketViewModel.isCreatingTicket,
                errorMessage = ticketViewModel.createTicketError
            )
        }

        composable(AppRoutes.MY_TICKETS) {

            val userId = authViewModel.currentUser?.uid
            LaunchedEffect(userId) {
                if (userId != null) {
                    ticketViewModel.refreshMyTickets(userId) { error ->
                        // Refresh error handling will be added later.
                    }
                }
            }
            if (userId != null) {

                val tickets by ticketViewModel
                    .getMyTickets(userId)
                    .collectAsState(initial = emptyList())

                MyTicketsScreen(
                    tickets = tickets,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(AppRoutes.ADMIN_DASHBOARD) {
            LaunchedEffect(Unit) {
                ticketViewModel.refreshAllTickets { error ->
                    // Error handling will be added later.
                }
            }
            val allTickets by ticketViewModel
                .getAllTickets()
                .collectAsState(initial = emptyList())
            AdminDashboardScreen(
                tickets = allTickets,
                onUpdateStatus = { ticketId, status, notes ->
                    ticketViewModel.updateTicketStatus(
                        ticketId = ticketId,
                        status = status,
                        resolutionNotes = notes,
                        onSuccess = {
                            // Room Flow updates the dashboard automatically.
                        },
                        onError = { error ->
                            // We'll add error feedback later.
                        }
                    )
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppRoutes.TECHNICIAN_DASHBOARD) {
            TechnicianDashboardScreen(
                onViewAvailableTickets = {
                    navController.navigate(AppRoutes.AVAILABLE_TICKETS)
                },
                onViewAssignedTickets = {
                    navController.navigate(AppRoutes.ASSIGNED_TICKETS)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppRoutes.AVAILABLE_TICKETS) {
            val technicianId = authViewModel.currentUser?.uid

            LaunchedEffect(technicianId) {
                if (technicianId != null) {
                    ticketViewModel.refreshAllTickets()
                }
            }

            if (technicianId != null) {
                val availableTickets by ticketViewModel
                    .getAvailableTickets()
                    .collectAsState(initial = emptyList())

                AvailableTicketsScreen(
                    tickets = availableTickets,
                    onAcceptTicket = { ticketId ->
                        ticketViewModel.acceptTicket(
                            ticketId = ticketId,
                            technicianId = technicianId
                        )
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(AppRoutes.ASSIGNED_TICKETS) {
            val technicianId = authViewModel.currentUser?.uid

            LaunchedEffect(technicianId) {
                if (technicianId != null) {
                    ticketViewModel.refreshAllTickets()
                }
            }
            if (technicianId != null) {
                val assignedTickets by ticketViewModel
                    .getAssignedTickets(technicianId)
                    .collectAsState(initial = emptyList())

                AssignedTicketsScreen(
                    tickets = assignedTickets,
                    onUpdateStatus = { ticketId, status, notes ->
                        ticketViewModel.updateTechnicianTicketStatus(
                            ticketId = ticketId,
                            status = status,
                            resolutionNotes = notes
                        )
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

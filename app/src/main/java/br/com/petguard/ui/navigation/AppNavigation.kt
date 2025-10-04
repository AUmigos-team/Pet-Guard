package br.com.petguard.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.repository.ReportRepository
import br.com.petguard.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val appDatabase = AppDatabase.getDatabase(context)
    val repository = ReportRepository(appDatabase)

    val bottomItems = listOf(
        BottomNavItem("home", "Denúncias", Icons.Filled.Home),
        BottomNavItem("new_inspection", "Visitas", Icons.Filled.Place),
        BottomNavItem("reports", "Relatórios", Icons.Filled.Check),
        BottomNavItem("settings", "Ajustes", Icons.Filled.Settings),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf("splash", "login", "register")) {
                BottomNavBar(navController = navController, items = bottomItems)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("login") { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) } // Nova tela
            composable("home") { HomeScreen(navController, repository) }
            composable("new_inspection") { NewInspectionScreen(navController, repository) }
            composable("pending_reports") { PendingReportsScreen(navController, repository) }
            composable("reports") { ReportsScreen(navController, repository) }
            composable("settings") { SettingsScreen(navController) }
        }

    }
}

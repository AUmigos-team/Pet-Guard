package br.com.petguard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.repository.ReportRepository
import br.com.petguard.ui.screens.HomeScreen
import br.com.petguard.ui.screens.LoginScreen
import br.com.petguard.ui.screens.NewInspectionScreen
import br.com.petguard.ui.screens.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val appDatabase = AppDatabase.getDatabase(context)
    val repository = ReportRepository(appDatabase)

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("new_inspection") { NewInspectionScreen(navController, repository) }
        composable("pending_reports") { PendingReportsScreen(navController, repository) }
        composable("reports") { ReportsScreen(navController, repository) }
        composable("settings") { SettingsScreen(navController) }
    }
}
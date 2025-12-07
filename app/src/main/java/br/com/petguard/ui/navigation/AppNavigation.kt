package br.com.petguard.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.repository.ReportRepository
import br.com.petguard.ui.screens.CommonUserLoginScreen
import br.com.petguard.ui.screens.CommonUserRegisterScreen
import br.com.petguard.ui.screens.HomeScreen
import br.com.petguard.ui.screens.InspectorUserLoginScreen
import br.com.petguard.ui.screens.InspectorUserRegisterScreen
import br.com.petguard.ui.screens.NewInspectionScreen
import br.com.petguard.ui.screens.PendingReportsScreen
import br.com.petguard.ui.screens.ReportsScreen
import br.com.petguard.ui.screens.SettingsScreen
import br.com.petguard.ui.screens.SplashScreen
import br.com.petguard.ui.screens.UserTypeSelectionScreen
import br.com.petguard.ui.screens.MyPendingReportsScreen
import br.com.petguard.ui.screens.MyCompletedReportsScreen
import androidx.compose.runtime.setValue
import br.com.petguard.data.database.User
import br.com.petguard.data.repository.UserRepository


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val appDatabase = AppDatabase.getDatabase(context)
    val repository = remember { ReportRepository(appDatabase, context) }

    val userRepository = remember { UserRepository(appDatabase) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var isCommonUser by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        currentUser = userRepository.getCurrentUser()
        isCommonUser = currentUser?.userType == "COMMON"
    }

    val bottomItems = listOf(
        BottomNavItem("home", "Denúncias", Icons.Filled.Home),
        BottomNavItem("new_inspection", "Visitas", Icons.Filled.Place),
        BottomNavItem("reports", "Relatórios", Icons.Filled.Check),
        BottomNavItem("settings", "Ajustes", Icons.Filled.Settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf("splash",
                    "common_login",
                    "common_register",
                    "inspector_register",
                    "inspector_login",
                    "user_type_selection")) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("new_inspection/{reportId}") { backStackEntry ->
                val reportId = backStackEntry.arguments?.getString("reportId")?.toLongOrNull() ?: 0L
                NewInspectionScreen(navController, repository, reportId)
            }
            composable("splash") { SplashScreen(navController) }
            composable("common_login") { CommonUserLoginScreen(navController) }
            composable("common_register") { CommonUserRegisterScreen(navController) }
            composable("inspector_register") { InspectorUserRegisterScreen(navController) }
            composable("inspector_login") { InspectorUserLoginScreen(navController) }
            composable("home") { HomeScreen(navController, repository) }
            composable("new_inspection") { NewInspectionScreen(navController, repository) }
            composable("pending_reports") { PendingReportsScreen(navController, repository) }
            composable("reports") { ReportsScreen(navController, repository) }
            composable("settings") { SettingsScreen(navController) }
            composable("user_type_selection") { UserTypeSelectionScreen(navController) }
            composable("my_pending_reports") { MyPendingReportsScreen(navController, repository) }
            composable("my_completed_reports") { MyCompletedReportsScreen(navController, repository) }
        }
    }
}

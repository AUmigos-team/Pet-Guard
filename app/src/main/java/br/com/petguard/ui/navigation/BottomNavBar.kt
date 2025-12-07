package br.com.petguard.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar(containerColor = Color(0xFF8A9A5B)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        var userType by remember { mutableStateOf("COMMON") }

        LaunchedEffect(Unit) {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val document = firestore.collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()
                userType = document.getString("userType") ?: "COMMON"
            } else {
                println("DEBUG BottomNavBar: No user logged in")
            }
        }

        NavigationBar(containerColor = Color(0xFF8A9A5B)) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val navItems = if (userType == "COMMON") {
                listOf(
                    BottomNavItem("home", "Home", Icons.Filled.Home),
                    BottomNavItem("new_inspection", "Visitas", Icons.Filled.Place),
                    BottomNavItem("my_completed_reports", "Concluídas", Icons.Filled.Check),
                    BottomNavItem("settings", "Ajustes", Icons.Filled.Settings)
                )
            } else {
                listOf(
                    BottomNavItem("home", "Home", Icons.Filled.Home),
                    BottomNavItem("new_inspection", "Visitas", Icons.Filled.Place),
                    BottomNavItem("reports", "Relatórios", Icons.Filled.Check),
                    BottomNavItem("settings", "Ajustes", Icons.Filled.Settings)
                )
            }

            navItems.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = Color(0xFF6E8050),
                        unselectedIconColor = Color.White.copy(alpha = 0.7f),
                        unselectedTextColor = Color.White.copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}
package com.example.sars

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph

// Simple enum to distinguish user types
enum class UserType {
    CITIZEN, ORGANIZATION
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    // State to simulate user type (Citizen by default)
    var userType by remember { mutableStateOf(UserType.CITIZEN) }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        Log.i("NavController", "Destination: ${destination.route}")
    }

    Scaffold(
        topBar = {
            // This is the "Simple Switch" for testing
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = if (userType == UserType.CITIZEN) "Citizen View" else "Org View")
                Switch(
                    checked = userType == UserType.ORGANIZATION,
                    onCheckedChange = { isChecked ->
                        userType = if (isChecked) UserType.ORGANIZATION else UserType.CITIZEN
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            graph = navGraphBuilder(navController, userType),
            modifier = Modifier.padding(innerPadding)
        )
    }
}

fun navGraphBuilder(navController: NavController, userType: UserType): NavGraph {
    return navController.createGraph(startDestination = "Login-Screen") {

        composable("Login-Screen") { LoginScreen(navController) }
        composable("Register-Screen") { RegisterScreen(navController) }
        composable("HeatMap") { HeatMap(navController) }
        composable("Plus-Screen") { CameraScreen(navController, onClose = {}) }

        // 🐾 ADOPTION FLOW (Conditional)
        composable("Adoption-Screen") {
            if (userType == UserType.ORGANIZATION) {
                AddPetScreen(navController) // Organizations see the "Add" screen
            } else {
                AdoptionScreen(navController) // Citizens see the "View" screen
            }
        }

        composable("Details-Screen/{petName}") { backStackEntry ->
            val petName = backStackEntry.arguments?.getString("petName") ?: ""
            DetailsScreen(petName, navController)
        }

    }
}

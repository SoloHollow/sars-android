package com.example.sars

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph

@Composable
fun Navigation() {
    val navController = rememberNavController()

    navController.addOnDestinationChangedListener { controller, destination, _ ->
        Log.i("NavController", "Destination: ${destination.route}")
    }

    NavHost(navController = navController, graph = navGraphBuilder(navController))
}

fun navGraphBuilder(navController: NavController): NavGraph {
    return navController.createGraph(startDestination = "Login-Screen") {

        // Login & Register
        composable("Login-Screen") { LoginScreen(navController) }
        composable("Register-Screen") { RegisterScreen(navController) }

        // HeatMap screen
        composable("HeatMap") { HeatMap(navController) }

        // Report Detail Screen
        composable("ReportDetailScreen") { ReportDetailScreen(navController) }

        // Camera (Plus-Screen) -> navigates to ReportDetailScreen
        composable("Plus-Screen") {
            CameraScreen(
                navController = navController,
                onClose = { navController.popBackStack() }
            )
        }

        // Adoption Screen
        composable("Adoption-Screen") { AdoptionScreen(navController) }

        //auto location
        composable("ReportDetailScreen/{street}") { backStackEntry ->
            val street = backStackEntry.arguments?.getString("street") ?: ""
            ReportDetailScreen(navController, detectedStreet = street)
        }

    }
}

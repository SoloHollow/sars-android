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
fun Navigation() {   //navController
    val navController = rememberNavController()

    navController.addOnDestinationChangedListener{navController,
                                                  destination,
                                                    arguments->
        Log.i("NavController","Destination: ${destination.route}")

    }


//navHost
    NavHost(navController = navController, graph = navGraphBuilder(navController))
}

//Nav graph

fun navGraphBuilder(navController: NavController):NavGraph
{

    return navController.createGraph(startDestination = "Login-Screen")
    {

        /*composable("Heat-Map")
        {
            HeatMap(navController)
        }*/

        composable("Adoption-Screen")
        {
            AdoptionScreen(navController)
        }

        composable("Plus-Screen")
        {
            CameraScreen (navController,onClose = { var showCamera = false })
        }
        composable("Login-Screen"){
            LoginScreen(navController)
        }
        composable("Register-Screen")
        {
            RegisterScreen(navController)
        }
        composable("HeatMap")
        {
            HeatMap(navController)
        }
        composable("Profile-Screen"){
            ProfileScreen(navController)
        }
    }
}
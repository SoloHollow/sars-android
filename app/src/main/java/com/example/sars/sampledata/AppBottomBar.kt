package com.example.sars.sampledata

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.jetbrainscomponents.R

@Composable
fun AppBottomBar(navController: NavController,selectedIndex: MutableState<Int>, onAddClick: () -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = selectedIndex.value == 0,
            onClick = { navController.navigate("Main-Screen")},
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = navItemColors()
        )
        NavigationBarItem(
            selected = selectedIndex.value == 1,
            onClick = { navController.navigate("Adoption-Screen")},
            icon = {Icon(
                painter = painterResource(id = R.drawable.ic_paw), // Example icon, replace with your actual paw icon
                contentDescription = "Profile"
            )},
            label = { Text("Adopt") },
            colors = navItemColors()
        )
        //remove to a floating button
//        NavigationBarItem(
//            selected = selectedIndex.value == 2,
//            onClick = {
//                selectedIndex.value = 2
//                Log.i("BottomNav", "Add clicked")
//                onAddClick()
//            },
//            icon = {
//                Box(
//                    modifier = Modifier
//                        .size(40.dp)
//                        .background(Color(0xFF7A7CAB), shape = CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.Black)
//                }
//            },
//            label = { Text("Add") },
//            colors = navItemColors()
//        )
        NavigationBarItem(
            selected = selectedIndex.value == 3,
            onClick = { selectedIndex.value = 3 },
            icon = {Icon(
                painter = painterResource(id = R.drawable.ic_info), // Example icon, replace with your actual paw icon
                contentDescription = "Profile"
            )},
            label = { Text("Info") },
            colors = navItemColors()
        )
        NavigationBarItem(
            selected = selectedIndex.value == 4,
            onClick = { selectedIndex.value = 4 },
            icon = {Icon(
                painter = painterResource(id = R.drawable.ic_acc), // Example icon, replace with your actual paw icon
                contentDescription = "Profile"
            )},
            label = { Text("Account") },
            colors = navItemColors()
        )
    }
}

@Composable
fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
    unselectedIconColor = MaterialTheme.colorScheme.secondary,
    selectedTextColor = MaterialTheme.colorScheme.onSurface,
    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
    indicatorColor = MaterialTheme.colorScheme.primary
)
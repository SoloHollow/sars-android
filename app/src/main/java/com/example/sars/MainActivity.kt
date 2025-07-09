package com.example.sars

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetbrainscomponents.ui.theme.JetbrainsComponentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetbrainsComponentsTheme {
                MainScreen()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { AppBottomBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    Log.i("MainScreen", "Click Me 1 clicked")
                }) {
                    Text("Click Me 1")
                }
                Button(onClick = {
                    Log.i("MainScreen", "Click Me 2 clicked")
                }) {
                    Text("Click Me 2")
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                text = "Pawnder",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        actions = {
            IconButton(onClick = { Log.i("TopBar", "Profile clicked") }) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
            }
            IconButton(onClick = { Log.i("TopBar", "More clicked") }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
            }
            IconButton(onClick = { Log.i("TopBar", "Search clicked") }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
        }
    )
}
@Composable
fun AppBottomBar() {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { Log.i("BottomNav", "Home clicked") },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = navItemColors()
        )
        NavigationBarItem(
            selected = false,
            onClick = { Log.i("BottomNav", "Filter 2 clicked") },
            icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 2") },
            label = { Text("Filter 2") },
            colors = navItemColors()
        )
        NavigationBarItem(
            selected = false,
            onClick = { Log.i("BottomNav", "Add clicked") },
            icon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF7A7CAB), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.Black)
                }
            },
            label = { Text("Add") },
            colors = navItemColors()
        )
        NavigationBarItem(
            selected = false,
            onClick = { Log.i("BottomNav", "Filter 4 clicked") },
            icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 4") },
            label = { Text("Filter 4") },
            colors = navItemColors()
        )
        NavigationBarItem(
            selected = false,
            onClick = { Log.i("BottomNav", "Filter 5 clicked") },
            icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 5") },
            label = { Text("Filter 5") },
            colors = navItemColors()
        )
    }
}
@Composable
fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color.Black,
    unselectedIconColor = Color.Black,
    selectedTextColor = Color.Black,
    unselectedTextColor = Color.Black,
    indicatorColor = MaterialTheme.colorScheme.primary
)
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    JetbrainsComponentsTheme {
        MainScreen()
    }
}

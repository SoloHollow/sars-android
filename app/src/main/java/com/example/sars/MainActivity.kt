package com.example.jetbrainscomponents

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetbrainscomponents.ui.theme.JetbrainsComponentsTheme
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background


import androidx.compose.material.icons.filled.Home




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
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(
                        text = "Tournament Tracker",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = {
                        Log.i("MainScreen", "Profile icon clicked")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = { Log.i("MainScreen", "Profile icon clicked") }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "MoreVert",
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = { Log.i("MainScreen", "Profile icon clicked") }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                }

            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { Log.i("BottomNav", "Home clicked") },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        indicatorColor = MaterialTheme.colorScheme.primary  
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { Log.i("BottomNav", "Filter 2 clicked") },
                    icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 2") },
                    label = { Text("Filter 2") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        indicatorColor = MaterialTheme.colorScheme.primary // Optional
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { Log.i("BottomNav", "Add clicked") },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(0xFF7A7CAB), // Correct hex color usage
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add",
                                tint = Color.Black
                            )
                        }
                    },
                    label = { Text("Add") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    )
                )


                NavigationBarItem(
                    selected = false,
                    onClick = { Log.i("BottomNav", "Filter 4 clicked") },
                    icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 4") },
                    label = { Text("Filter 4") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        indicatorColor = MaterialTheme.colorScheme.primary // Optional
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { Log.i("BottomNav", "Filter 5 clicked") },
                    icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 5") },
                    label = { Text("Filter 5") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        indicatorColor = MaterialTheme.colorScheme.primary // Optional
                    )
                )
            }
        }


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
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    JetbrainsComponentsTheme {
        MainScreen()
    }
}

package com.example.sars

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import android.Manifest
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.jetbrainscomponents.ui.theme.JetbrainsComponentsTheme
import androidx.navigation.compose.rememberNavController as rememberNavController

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

/*@Composable
fun AppBottomBar(navController: NavController,selectedIndex: MutableState<Int>) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        NavigationBarItem(
            selected = selectedIndex.value == 0,
            onClick = { navController.navigate("Main-Screen") },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = navItemColors()
        )

        NavigationBarItem(
            selected = selectedIndex.value == 1,
            onClick = {navController.navigate("Adoption-Screen")},
            icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 2") },
            label = { Text("Filter 2") },
            colors = navItemColors()
        )

        NavigationBarItem(
            selected = selectedIndex.value == 2,
            onClick = {
                selectedIndex.value = 2
                Log.i("BottomNav", "Add clicked")
            },
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
            selected = selectedIndex.value == 3,
            onClick = {
                selectedIndex.value = 3
                Log.i("BottomNav", "Filter 4 clicked")
            },
            icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 4") },
            label = { Text("Filter 4") },
            colors = navItemColors()
        )

        NavigationBarItem(
            selected = selectedIndex.value == 4,
            onClick = {
                selectedIndex.value = 4
                Log.i("BottomNav", "Filter 5 clicked")
            },
            icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 5") },
            label = { Text("Filter 5") },
            colors = navItemColors()
        )
    }
}
*/

@Composable
fun AppBottomBar(navController: NavController,selectedIndex: MutableState<Int>, onAddClick: () -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
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
            icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 2") },
            label = { Text("Filter 2") },
            colors = navItemColors()
        )
        NavigationBarItem(
            selected = selectedIndex.value == 2,
            onClick = {
                selectedIndex.value = 2
                Log.i("BottomNav", "Add clicked")
                onAddClick()
            },
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
            selected = selectedIndex.value == 3,
            onClick = { selectedIndex.value = 3 },
            icon = { Icon(Icons.Filled.Filter, contentDescription = "Filter 4") },
            label = { Text("Filter 4") },
            colors = navItemColors()
        )
        NavigationBarItem(
            selected = selectedIndex.value == 4,
            onClick = { selectedIndex.value = 4 },
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

/*@Composable
fun MainScreen(navController: NavController) {


    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { AppBottomBar(navController, selectedIndex) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Your content here
        }
    }
}

 */

@Composable
fun RequestCameraPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val permissionGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) onPermissionGranted()
        }
    )

    LaunchedEffect(Unit) {
        if (permissionGranted) {
            onPermissionGranted()
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    val selectedIndex = remember { mutableIntStateOf(0) }
    var permissionGranted by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }

    if (!permissionGranted) {
        RequestCameraPermission {
            permissionGranted = true
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Requesting Camera Permission...")
        }
    } else if (showCamera) {
        CameraScreen(navController,onClose = { showCamera = false })
    } else {
        Scaffold(
            topBar = { AppTopBar() },
            bottomBar = { AppBottomBar( navController ,selectedIndex, onAddClick = { showCamera = true }) }
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
                    Button(onClick = { Log.i("MainScreen", "Click Me 1 clicked") }) {
                        Text("Click Me 1")
                    }
                    Button(onClick = { Log.i("MainScreen", "Click Me 2 clicked") }) {
                        Text("Click Me 2")
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    JetbrainsComponentsTheme {
        Navigation()
    }
}
package com.example.sars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetbrainscomponents.ui.theme.JetbrainsComponentsTheme



/*
@Composable
fun AdoptionScreen(navController: NavController) {
    // Selected index state for bottom navigation
    val selectedIndex = remember { mutableIntStateOf(0) }
    val navController = rememberNavController()

    Scaffold(
        topBar = {},
        bottomBar = { AppBottomBar(navController,selectedIndex) }
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
            ) {}
        }
    }    var showCamera by remember { mutableStateOf(false) }

}
*/

@Composable
fun AdoptionScreen(navController: NavController) {
    val selectedIndex = remember { mutableIntStateOf(1) }
    var showCamera by remember { mutableStateOf(false) }

    val pets = remember { listOf("CAT", "DOG", "3") }
    var currentIndex by remember { mutableStateOf(0) } // Correct delegation


    Scaffold(
        bottomBar = { AppBottomBar(navController,selectedIndex, onAddClick = { showCamera = true }) }

    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (currentIndex <  pets.size) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(300.dp)
                        .background(Color.LightGray, RoundedCornerShape(16.dp))
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures { _, dragAmount ->
                                if (dragAmount > 0) {  }
                                else {  }
                                currentIndex++
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(pets[currentIndex], fontSize = 32.sp)                }
            } else {
                Text("No more pets", Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAdoptionScreen() {
    val navController = rememberNavController()
    JetbrainsComponentsTheme {
        AdoptionScreen(navController)
    }
}

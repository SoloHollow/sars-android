package com.example.sars

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetbrainscomponents.ui.theme.JetbrainsComponentsTheme

@Composable
fun FloatingActionButtonWithNav(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(20.dp)//16 on left
                .align(Alignment.BottomStart),//BottomEnd
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            onClick = {
                navController.navigate("Plus-Screen")
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Camera,
                contentDescription = "Open Camera"
            )
        }
    }
}

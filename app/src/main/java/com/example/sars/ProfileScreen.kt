package com.example.sars

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sars.sampledata.AppBottomBar
import com.example.sars.sampledata.AppTopBar
import com.example.jetbrainscomponents.R


@Composable
fun ProfileScreen(navController: NavController) {
    val selectedIndex = remember { mutableIntStateOf(4) }
    var showCamera by remember { mutableStateOf(false) }

    val username = "John Doe" // Replace with db thing
    val email = "johndoe@email.com" // Replace with db thing
    val bio = "Pet Lover" // Replace with db thing

    Scaffold(topBar = { AppTopBar(title = username) }, bottomBar = {
        AppBottomBar(
            navController, selectedIndex, onAddClick = { showCamera = true })
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Profile
            Image(
                painter = painterResource(id = R.drawable.ic_paw), // Replace with pfp
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = username)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = email)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = bio)
            Spacer(modifier = Modifier.height(32.dp))

            ProfileButton(text = "Edit Profile", icon = Icons.Default.Edit) {
                // edit profile
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProfileButton(text = "Privacy Policy", icon = Icons.Default.Lock) {
                // questionable button
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProfileButton(text = "Logout", icon = Icons.Default.ExitToApp) {
                navController.navigate("Login-Screen")
            }
        }
    }
}

@Composable
fun ProfileButton(
    text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit
) {
    Button(
        onClick = onClick, modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text)
        }
    }
}

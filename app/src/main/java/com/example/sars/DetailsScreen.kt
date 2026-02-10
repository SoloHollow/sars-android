package com.example.sars

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sars.sampledata.PetRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    petName: String,
    navController: NavController
) {

    val pet = PetRepository.pets.find { it.name == petName } ?: return

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pet Details") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                pet.name,
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Breed: ${pet.breed}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Age: ${pet.age}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(16.dp))

            Text(
                pet.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
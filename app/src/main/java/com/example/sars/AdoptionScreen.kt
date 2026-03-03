package com.example.sars

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sars.sampledata.PetRepository
import kotlinx.coroutines.launch

@Composable
fun AdoptionScreen(navController: NavController) {

    var showCamera by remember { mutableStateOf(false) }

    val pets = PetRepository.pets
    var index by remember { mutableStateOf(0) }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

            if (index >= pets.size) {
                Text("No more pets 🐾", fontSize = 22.sp)
                return@Box
            }

        val pet = pets[index]
        val offsetX = remember { Animatable(0f) }
        val scope = rememberCoroutineScope()

            Card(
                modifier = Modifier
                    .size(340.dp, 500.dp) // Adjusted size to fit image + text
                    .offset { IntOffset(offsetX.value.toInt(), 0) }
                    .graphicsLayer {
                        rotationZ = offsetX.value / 40f
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { _, drag ->
                                scope.launch {
                                    offsetX.snapTo(offsetX.value + drag.x)
                                }
                            },
                            onDragEnd = {
                                scope.launch {
                                    if (offsetX.value > 300 || offsetX.value < -300) {
                                        offsetX.animateTo(
                                            if (offsetX.value > 0) 1000f else -1000f
                                        )
                                        index++
                                        offsetX.snapTo(0f)
                                    } else {
                                        offsetX.animateTo(0f)
                                    }
                                }
                            }
                        )
                    },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // 🐾 1. THE IMAGE COMPONENT (Added this)
                    Image(
                        painter = painterResource(id = pet.imageRes),
                        contentDescription = "Photo of ${pet.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp) // Takes up the top portion of the card
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        contentScale = ContentScale.Crop // Ensures image fills the area without distortion
                    )

                    // 2. Text and Buttons Section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = pet.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${pet.breed} • ${pet.age}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                navController.navigate("Details-Screen/${pet.name}")
                            }
                        ) {
                            Text("View Details")
                        }
                    } // Closes the inner Column
                } // Closes the Card's Column
            } // Closes the Card
        } // Closes the Box
} // Closes the AdoptionScreen function
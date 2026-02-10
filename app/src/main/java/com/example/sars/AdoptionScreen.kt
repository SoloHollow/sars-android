package com.example.sars

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sars.sampledata.AppBottomBar
import com.example.sars.sampledata.PetRepository
import kotlinx.coroutines.launch

@Composable
fun AdoptionScreen(navController: NavController) {

    val selectedIndex = remember { mutableIntStateOf(1) }
    var showCamera by remember { mutableStateOf(false) }

    val pets = PetRepository.pets
    var index by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                navController,
                selectedIndex,
                onAddClick = { showCamera = true }
            )
        }
    ) { padding ->

        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {

            // No more pets
            if (index >= pets.size) {
                Text("No more pets 🐾", fontSize = 22.sp)
                return@Box
            }

            val pet = pets[index]

            val offsetX = remember { Animatable(0f) }
            val scope = rememberCoroutineScope()

            Card(
                modifier = Modifier
                    .size(320.dp, 420.dp)
                    .offset { IntOffset(offsetX.value.toInt(), 0) }
                    // ⭐ rotation effect (optional but nice)
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

                                        // Swipe away animation
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
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    Text(pet.name, fontSize = 28.sp)
                    Text(pet.breed)
                    Text(pet.age)

                    Button(
                        onClick = {
                            navController.navigate(
                                "Details-Screen/${pet.name}"
                            )
                        }
                    ) {
                        Text("View Details")
                    }
                }
            }
        }
    }
}
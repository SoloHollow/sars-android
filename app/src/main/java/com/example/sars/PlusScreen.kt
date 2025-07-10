package com.example.sars

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageProxy


@OptIn(ExperimentalMaterial3Api::class)
@Composable
<<<<<<< HEAD:app/src/main/java/com/example/sars/PlusScreen.kt
fun PlusScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Plus screen content goes here")
        Button(onClick = {
            navController.popBackStack() // Go back to previous screen
        }) {
            Text(text = "Go Back")
=======
fun CameraScreen(onClose: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA)
>>>>>>> 88108c3f8950c2fd268b613745f35865662bd35e:app/src/main/java/com/example/sars/plusScreen.kt
        }
    }

    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Camera") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val currentSelector = cameraController.cameraSelector
                        cameraController.cameraSelector =
                            if (currentSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            else
                                CameraSelector.DEFAULT_BACK_CAMERA
                    }) {
                        Icon(Icons.Default.Cameraswitch, contentDescription = "Switch Camera")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        controller = cameraController
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Bottom bar with buttons
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gallery button (placeholder)
                IconButton(onClick = {
                    Log.d("CameraScreen", "Gallery button clicked")
                }) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery")
                }

                // Capture button
                IconButton(
                    onClick = {
                        takePhoto(context, cameraController) { bitmap ->
                            Log.d("CameraScreen", "Photo taken!")
                            capturedImage = bitmap
                        }
                    },
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = "Capture Photo",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Flash button (static placeholder)
                IconButton(onClick = {
                    Log.d("CameraScreen", "Flash button clicked")
                }) {
                    Icon(Icons.Default.FlashOn, contentDescription = "Flash")
                }
            }

            // Show captured image in a dialog
            capturedImage?.let { image ->
                AlertDialog(
                    onDismissRequest = { capturedImage = null },
                    confirmButton = {
                        TextButton(onClick = { capturedImage = null }) {
                            Text("Close")
                        }
                    },
                    title = { Text("Captured Photo") },
                    text = {
                        Image(
                            bitmap = image.asImageBitmap(),
                            contentDescription = "Captured",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                    }
                )
            }
        }
    }

    // Bind controller to lifecycle
    LaunchedEffect(Unit) {
        cameraController.bindToLifecycle(lifecycleOwner)
    }
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val rotationDegrees = image.imageInfo.rotationDegrees
                val bitmap = image.toBitmap()
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                val rotated = Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                )
                image.close()
                onPhotoTaken(rotated)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraScreen", "Capture failed", exception)
            }
        }
    )
}

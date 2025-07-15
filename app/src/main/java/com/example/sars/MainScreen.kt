package com.example.sars

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.jetbrainscomponents.ui.theme.JetbrainsComponentsTheme
import com.example.sars.sampledata.AppBottomBar
import com.example.sars.sampledata.AppTopBar

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

/*@Composable
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
            }
        }
    }
}*/
@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    JetbrainsComponentsTheme {
        Navigation()


    }
}
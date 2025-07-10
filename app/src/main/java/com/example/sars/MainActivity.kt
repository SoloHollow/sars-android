package com.example.sars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetbrainscomponents.ui.theme.JetbrainsComponentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetbrainsComponentsTheme {
                HeatMap()
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    JetbrainsComponentsTheme {
        MainScreen()
    }
}

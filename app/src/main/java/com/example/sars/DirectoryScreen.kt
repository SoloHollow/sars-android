package com.example.sars

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DirectoryScreen(navController: NavController) {
    val context = LocalContext.current
    val apiService = remember { ApiService.create(context) }
    var entries by remember { mutableStateOf<List<DirectoryEntry>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = apiService.getDirectoryEntries()
            entries = response.entries ?: emptyList()
        } catch (e: Exception) {
            Log.e("DirectoryScreen", "Directory load error", e)
            errorMessage = "Failed to load directory: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (isLoading || errorMessage != null) Arrangement.Center else Arrangement.Top
    ) {
        if (isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Loading directory...")
        } else if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
        } else if (entries.isEmpty()) {
            Text(text = "No entries found.")
        } else {
            // Screen Title
            Text(
                text = "Directory",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp).align(Alignment.Start)
            )
            LazyColumn {
                items(entries) { entry ->
                    DirectoryItem(entry)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun DirectoryItem(entry: DirectoryEntry) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = entry.name ?: "Unknown Name", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = entry.type ?: "General", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            if (!entry.content.isNullOrEmpty()) {
                Text(text = entry.content, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(text = "Contact: ${entry.contact ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Address: ${entry.address ?: "N/A"}${if (entry.state != null) ", ${entry.state}" else ""}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

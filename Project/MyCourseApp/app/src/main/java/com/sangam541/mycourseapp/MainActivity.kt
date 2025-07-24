package com.sangam541.mycourseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.*
import com.sangam541.mycourseapp.ui.theme.MyCourseAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCourseAppTheme {
                AppNavigation()
            }
        }
    }
}


// ---------------------------
// NAVIGATION SETUP
// ---------------------------
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var darkMode by remember { mutableStateOf(false) }

    MyCourseAppTheme(darkTheme = darkMode) {
        NavHost(navController = navController, startDestination = "notes") {
            composable("notes") { NoteAppScreen(navController) }
            composable("settings") { SettingsScreen(navController, darkMode) { darkMode = it } }
        }
    }
}


// ---------------------------
// NOTE SCREEN
// ---------------------------
@Composable
fun NoteAppScreen(navController: NavController) {
    var noteText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = noteText,
                onValueChange = { noteText = it },
                modifier = Modifier.weight(1f),
                label = { Text("Enter note") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (noteText.isNotBlank()) {
                    notes = notes + noteText
                    noteText = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(notes) { note ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = note,
                        modifier = Modifier.padding(8.dp)
                    )
                    TextButton(onClick = {
                        notes = notes.toMutableList().also { it.remove(note) }
                    }) {
                        Text("Delete")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("settings") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Go to Settings")
        }
    }
}

// ---------------------------
// SETTINGS SCREEN
// ---------------------------
@Composable
fun SettingsScreen(
    navController: NavController,
    darkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode")
            Switch(
                checked = darkMode,
                onCheckedChange = { onToggleDarkMode(it) }
            )

        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            navController.navigate("notes")
        }) {
            Text("Back to Notes")
        }
    }
}

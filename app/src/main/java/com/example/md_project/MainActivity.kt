package com.example.md_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.md_project.ui.theme.MD_ProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MD_ProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Navigation between screens
@Composable
fun NavigationScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // for all screens
    var plants by remember { mutableStateOf(listOf("Monstera", "Basil", "fern")) }
    var selectedPlant by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = "dashboard", modifier = modifier) {
        composable("dashboard") {
            DashboardScreen(
                navController = navController,
                plants = plants,
                onPlantClick = { plant -> selectedPlant = plant }
            )
        }
        composable("add") {
            AddPlantScreen(
                navController = navController,
                onSave = { name -> plants = plants + name } //add plant to the end of the list
            )
        }
        composable("plant") {
            PlantScreen(plantName = selectedPlant)
        }
    }
}
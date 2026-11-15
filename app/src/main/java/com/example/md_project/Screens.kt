package com.example.md_project

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Screen 1: Overview of all plants
@Composable
fun DashboardScreen(
    navController: NavController,
    plants: List<String>,
    onPlantClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("My Home", fontSize = 28.sp)

        // each plant has its own card
        for (plant in plants) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onPlantClick(plant)
                        navController.navigate("plant")
                    }
            ) {
                Text(plant, fontSize = 20.sp, modifier = Modifier.padding(16.dp))
            }
        }

        Button(onClick = { navController.navigate("add") }) {
            Text("Add plant")
        }
    }
}

// Screen 2: add plants (implicit intent)
@Composable
fun AddPlantScreen(
    navController: NavController,
    onSave: (String) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var photo by remember { mutableStateOf<Bitmap?>(null) }

    // open galery and select picture
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            photo = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Add plant", fontSize = 28.sp)

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Pick photo")
        }

        // only show picture if selected
        photo?.let { btm ->
            Image(
                bitmap = btm.asImageBitmap(),
                contentDescription = "Plant photo",
                modifier = Modifier.size(200.dp)
            )
        }

        Button(onClick = {
            if (name.isNotBlank()) {
                onSave(name)
                navController.popBackStack()
            }
        }) {
            Text("Save")
        }
    }
}

// Screen 3: Plant details
@Composable
fun PlantScreen(plantName: String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(plantName, fontSize = 28.sp)
        Text("Light not measured yet")

        Button(onClick = {
            // Explicit Intent with data
            val intent = Intent(context, LightMeterActivity::class.java)
            intent.putExtra("plantName", plantName)
            context.startActivity(intent)
        }) {
            Text("Measure light")
        }
    }
}
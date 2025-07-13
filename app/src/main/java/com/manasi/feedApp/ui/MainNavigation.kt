package com.manasi.feedApp.ui
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.V
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val items = listOf("feed", "camera", "gallery")
    val icons = listOf(Icons.Filled.Feed, Icons.Filled.Camera, Icons.Filled.VideoLibrary)
    val labels = listOf("Feed", "Camera", "Gallery")
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = labels[index]) },
                        label = { Text(labels[index]) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(screen) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(navController, startDestination = "feed", Modifier.padding(it)) {
            composable("feed") { FeedScreen() }
            composable("camera") { CameraScreen(navController) }
            composable("gallery") { GalleryScreen() }
        }
    }
}

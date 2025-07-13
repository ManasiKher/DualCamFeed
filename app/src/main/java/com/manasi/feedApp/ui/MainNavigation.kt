package com.manasi.feedApp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Feed,
        Screen.Camera,
        Screen.Gallery
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Feed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Feed.route) {
                FeedScreen()
            }
            composable(Screen.Camera.route) {
                WithCameraAndAudioPermission {
                    CameraScreen(navController)
                }
            }
            composable(Screen.Gallery.route) {
                GalleryScreen()
            }
        }
    }
}

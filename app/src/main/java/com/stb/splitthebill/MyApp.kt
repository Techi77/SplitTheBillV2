package com.stb.splitthebill

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stb.registration.RegistrationScreen

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("registration") { RegistrationScreen(navController) }
        //composable("details") { DetailsScreen(navController) }
    }
}
package com.stb.splitthebill

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stb.registration.RegistrationScreen
import kotlinx.serialization.Serializable

@Serializable
object Registration

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Registration) {
        composable <Registration> { RegistrationScreen() }
        //composable("details") { DetailsScreen(navController) }
    }
}
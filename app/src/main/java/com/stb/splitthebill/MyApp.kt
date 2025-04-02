package com.stb.splitthebill

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stb.main.MainScreen
import com.stb.preferences.DataStoreManager
import com.stb.registration.RegistrationScreen
import kotlinx.serialization.Serializable

@Composable
fun MyApp(applicationContext: Context) {
    val navController = rememberNavController()
    val dataStoreManager = remember { DataStoreManager(applicationContext) }
    val userData by dataStoreManager.getAuthorizedUserFlow.collectAsState(initial = null)

    if (userData != null) {
        LaunchedEffect(Unit) {
            navController.navigate(Main) { popUpTo(Registration) }
        }
    }

    NavHost(navController, startDestination = userData?.let { Main } ?: Registration) {
        composable<Registration> {
            RegistrationScreen(
                onRegistrationComplete = {
                    navController.navigate(Main)
                }
            )
        }
        composable<Main> { MainScreen() }
    }
}

@Serializable
object Registration

@Serializable
object Main
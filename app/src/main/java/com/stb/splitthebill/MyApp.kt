package com.stb.splitthebill

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.stb.editlist.ListMainScreen
import com.stb.main.MainScreen
import com.stb.main.MainViewModel.Companion.LIST_ID
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
        composable<Main> {
            MainScreen(
                navigateToListDetail = { listId ->
                    navController.navigate(
                        route = "$EditList/$listId"
                    )
                }
            )
        }
        composable(
            route = "$EditList/{$LIST_ID}",
            arguments = listOf(navArgument(LIST_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString(LIST_ID) ?: ""
            ListMainScreen(
                listId = listId,
                goBack = {navController.popBackStack()}
            )
        }
    }
}

@Serializable object Registration

@Serializable object Main
@Serializable object EditList
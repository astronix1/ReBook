package com.example.rebook

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(navController: NavHostController, startDestination: String){
    NavHost(
        navController=navController,
        startDestination = startDestination
    ){
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
    }
}
package com.example.rebook

sealed class Screen(val route: String) {
    object HomeScreen : Screen(route = "home_screen")
    object LoginScreen : Screen(route = "login_screen")
}
package com.example.rebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.rebook.ui.theme.ReBookTheme
import com.google.firebase.auth.FirebaseAuth
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReBookTheme {
                val navController = rememberNavController()
                val auth = FirebaseAuth.getInstance()
                val startDestination = if (auth.currentUser != null) {
                    Screen.HomeScreen.route // Open Home if user is logged in
                } else {
                    Screen.LoginScreen.route // Otherwise, open Login
                }

                SetupNavGraph(navController = navController, startDestination = startDestination)
            }
        }
    }
}

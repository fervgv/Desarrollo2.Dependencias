package com.example.desarrollo2.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.desarrollo2.ui.screens.HomeScreen
import com.example.desarrollo2.ui.screens.InitialScreen
import com.example.desarrollo2.ui.screens.LoginScreen
import com.example.desarrollo2.ui.screens.SignUpScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth){
    NavHost(navController = navHostController, startDestination = "initial"){
        composable("initial"){
            InitialScreen(
                navigateToLogin = { navHostController.navigate("login") },
                navigateToSignUp = { navHostController.navigate("signUp") }
            )
        }
        composable("login"){
            LoginScreen(auth){ navHostController.navigate("home") }
        }
        composable("signUp"){
            SignUpScreen(auth) {navHostController.navigate("home")}
        }
        composable("home"){
            HomeScreen()
        }
    }
}
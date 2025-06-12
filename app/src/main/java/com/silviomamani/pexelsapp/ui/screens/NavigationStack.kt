package com.silviomamani.pexelsapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.silviomamani.pexelsapp.ui.screens.register.RegisterScreen
import com.silviomamani.pexelsapp.ui.screens.homescreen.HomeScreen
import com.silviomamani.pexelsapp.ui.screens.login.LoginScreen
import com.silviomamani.pexelsapp.ui.screens.pexelsdetail.PexelsDetailScreen
import com.silviomamani.pexelsapp.ui.screens.pexelslist.PexelsListScreen
import com.silviomamani.pexelsapp.ui.screens.pexelsvideodetail.PexelsVideoDetailScreen
import com.silviomamani.pexelsapp.ui.screens.splash.SplashScreen
@Composable
fun NavigationStack(
    onGoogleLoginClick: () -> Unit,
    navController: NavHostController,
    onLogoutClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route
    ) {
        composable(route = Screens.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(route = Screens.Login.route) {
            LoginScreen(
                onGoogleLoginClick = onGoogleLoginClick,
                onRegisterClick = {
                    navController.navigate(Screens.Register.route)
                },
                navController = navController
            )
        }

        composable(route = Screens.Register.route) {
            RegisterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screens.Home.route) {
            HomeScreen(
                navController = navController,
                userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Usuario",
                onLogoutClick = onLogoutClick
            )
        }

        composable(route = Screens.PexelsList.route) {
            PexelsListScreen(
                navController = navController
            )
        }

        composable(route = Screens.PexelsDetail.route + "/{pexelsId}") { backStackEntry ->
            val pexelsId = backStackEntry.arguments?.getString("pexelsId")?.toIntOrNull() ?: 0
            PexelsDetailScreen(
                pexelsId = pexelsId,
                navController = navController
            )
        }

        composable(route = Screens.PexelsVideosDetail.route + "/{videoId}") { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")?.toIntOrNull() ?: 0
            PexelsVideoDetailScreen(
                videoId = videoId,
                navController = navController
            )
        }
    }
}
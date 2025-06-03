package com.silviomamani.pexelsapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.silviomamani.pexelsapp.ui.screens.login.LoginScreen
import com.silviomamani.pexelsapp.ui.screens.pexelsdetail.PexelsDetailScreen
import com.silviomamani.pexelsapp.ui.screens.pexelslist.PexelsListScreen
import com.silviomamani.pexelsapp.ui.screens.splash.SplashScreen

@Composable
fun NavigationStack(
    onGoogleLoginClick: () -> Unit,
    navController : NavHostController
){

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route
    ){
        composable(route = Screens.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(route = Screens.Login.route){
            LoginScreen(onGoogleLoginClick, navController = navController)
        }
        composable(route = Screens.PexelsList.route){
            PexelsListScreen(navController = navController)

        }
        composable(route = Screens.PexelsDetail.route + "/{pexelsId}"){ it ->
            var id = it.arguments?.getString("pexelsId")
            val pexelsId = id?.toIntOrNull()
           PexelsDetailScreen(pexelsId ?: 0)

        }

    }
}
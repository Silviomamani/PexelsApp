package com.silviomamani.pexelsapp.ui.screens.splash

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silviomamani.pexelsapp.ui.screens.Screens


@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){
    // Lanzar efecto al entrar a la pantalla
    LaunchedEffect(Unit) {
        delay(2000) // Espera 2 segundos
        navController.navigate(Screens.Login.route) {
            popUpTo("splash") { inclusive = true } // Elimina splash del backstack
        }
    }

    // UI del splash
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "PexelsApp",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

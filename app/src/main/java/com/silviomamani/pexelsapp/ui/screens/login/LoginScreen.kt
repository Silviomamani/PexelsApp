package com.silviomamani.pexelsapp.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.silviomamani.pexelsapp.ui.screens.Screens

@Composable
fun LoginScreen(
    onGoogleLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    vm: LoginScreenViewModel = viewModel()
){
    LaunchedEffect(Unit) {
        vm.uiEvent.collect {
            event ->
            navController.navigate(Screens.PexelsList.route){
            popUpTo(Screens.Login.route){ inclusive = true}
        }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Button(
            onClick = onGoogleLoginClick,
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Login con Google", color = Color.Black)
        }
    }

}
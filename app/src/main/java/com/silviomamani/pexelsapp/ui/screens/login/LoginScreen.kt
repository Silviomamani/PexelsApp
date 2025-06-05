package com.silviomamani.pexelsapp.ui.screens.login


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.silviomamani.pexelsapp.ui.screens.Screens


@Composable
fun LoginScreen(
    onGoogleLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    // Verificar si ya está logueado
    LaunchedEffect(Unit) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            navController.navigate(Screens.Home.route) {
                popUpTo(Screens.Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
            .statusBarsPadding()
    ) {
        // Header con flecha de regreso
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }

        // Contenido principal centrado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de bienvenida
            Text(
                text = "Bienvenido a",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )

            Text(
                text = "PexelsApp",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Título de iniciar sesión
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = TextAlign.Start
            )

            // Campo Email (solo visual)
            TextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Email", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFD4E6D4),
                    unfocusedContainerColor = Color(0xFFD4E6D4),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = false
            )

            // Campo Contraseña (solo visual)
            TextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Contraseña", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFD4E6D4),
                    unfocusedContainerColor = Color(0xFFD4E6D4),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                enabled = false
            )

            // Botón Entrar (solo visual)
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8BC34A),
                    disabledContainerColor = Color(0xFF8BC34A)
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = false
            ) {
                Text(
                    "Entrar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Texto "¿Olvidaste tu contraseña?" (solo visual)
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Texto "¿No tienes cuenta? Regístrate" (solo visual)
            Row {
                Text(
                    text = "¿No tienes cuenta? ",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Regístrate",
                    color = Color(0xFF2196F3),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Divisor con "O"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.Gray,
                    thickness = 1.dp
                )
                Text(
                    text = " O ",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.Gray,
                    thickness = 1.dp
                )
            }

            // Botón de Google (FUNCIONAL)
            Button(
                onClick = onGoogleLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🔍",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        "Iniciar sesión con Google",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
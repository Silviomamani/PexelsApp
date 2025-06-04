package com.silviomamani.pexelsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.silviomamani.pexelsapp.ui.screens.NavigationStack
import com.silviomamani.pexelsapp.ui.screens.Screens
import com.silviomamani.pexelsapp.ui.screens.pexelslist.PexelsListScreen
import com.silviomamani.pexelsapp.ui.theme.PexelsAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ✅ Inicializar Firebase correctamente
        FirebaseApp.initializeApp(this)

        // ✅ Configurar Google Sign-In
        googleSignInClient = GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

        setContent {
            navController = rememberNavController()

            var isLoading by remember { mutableStateOf(false) }
            var showAccountChooser by remember { mutableStateOf(false) }
            var availableAccounts by remember { mutableStateOf<List<GoogleSignInAccount>>(emptyList()) }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                isLoading = true
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    signInWithGoogleAccount(account) { success ->
                        isLoading = false
                        if (success) {
                            navController.navigate(Screens.PexelsList.route) {
                                popUpTo(Screens.Login.route) { inclusive = true }
                            }
                        }
                    }
                } catch (e: ApiException) {
                    isLoading = false
                    Log.e("AUTH", "Error en Google Sign-In", e)
                }
            }

            // Función para manejar el click de Google Sign In
            val handleGoogleLogin = {
                val lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this@MainActivity)

                if (lastSignedInAccount != null) {
                    // Si hay una cuenta previa, mostrar selector
                    availableAccounts = listOf(lastSignedInAccount)
                    showAccountChooser = true
                } else {
                    // Si no hay cuenta previa, iniciar sign in directo
                    launcher.launch(googleSignInClient.signInIntent)
                }
            }

            PexelsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box {
                        NavigationStack(
                            onGoogleLoginClick = handleGoogleLogin,
                            navController = navController,
                            onLogoutClick = {
                                FirebaseAuth.getInstance().signOut()
                                googleSignInClient.signOut() // También cerrar sesión de Google
                                navController.navigate(Screens.Login.route) {
                                    popUpTo(Screens.PexelsList.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        )

                        // Loading overlay
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        // Account Chooser Dialog
                        if (showAccountChooser) {
                            AccountChooserDialog(
                                accounts = availableAccounts,
                                onAccountSelected = { account ->
                                    showAccountChooser = false
                                    isLoading = true
                                    signInWithGoogleAccount(account) { success ->
                                        isLoading = false
                                        if (success) {
                                            navController.navigate(Screens.PexelsList.route) {
                                                popUpTo(Screens.Login.route) { inclusive = true }
                                            }
                                        }
                                    }
                                },
                                onAddNewAccount = {
                                    showAccountChooser = false
                                    // Cerrar sesión previa para forzar selector de cuentas
                                    googleSignInClient.signOut().addOnCompleteListener {
                                        launcher.launch(googleSignInClient.signInIntent)
                                    }
                                },
                                onDismiss = {
                                    showAccountChooser = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun signInWithGoogleAccount(
        account: GoogleSignInAccount,
        onComplete: (Boolean) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { authResult ->
                onComplete(authResult.isSuccessful)
                if (!authResult.isSuccessful) {
                    Log.e("AUTH", "Error en Firebase Auth", authResult.exception)
                }
            }
    }
}

@Composable
fun AccountChooserDialog(
    showDialog: Boolean = true, // Parámetro opcional por compatibilidad
    accounts: List<GoogleSignInAccount>,
    onAccountSelected: (GoogleSignInAccount) -> Unit,
    onAddNewAccount: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!showDialog) return
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Elegir cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                accounts.forEach { account ->
                    AccountItem(
                        account = account,
                        onClick = { onAccountSelected(account) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onAddNewAccount,
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Usar otra cuenta")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AccountItem(
    account: GoogleSignInAccount,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar circular con inicial
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFF4285F4),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = account.displayName?.firstOrNull()?.uppercase() ?: "?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = account.displayName ?: "Usuario",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = account.email ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Checkmark o icono opcional
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
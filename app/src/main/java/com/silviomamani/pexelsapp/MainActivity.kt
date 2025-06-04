package com.silviomamani.pexelsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
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

            // ✅ Registrar launcher ADENTRO del setContent (Firebase ya está inicializado)
            var isLoading by remember { mutableStateOf(false) }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                isLoading = true
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { authResult ->
                            isLoading = false
                            if (authResult.isSuccessful) {
                                navController.navigate(Screens.PexelsList.route) {
                                    popUpTo(Screens.Login.route) { inclusive = true }
                                }
                            } else {
                                // Handle error
                            }
                        }
                } catch (e: ApiException) {
                    isLoading = false
                    Log.e("AUTH", "Error en Google Sign-In", e)
                }
            }

            PexelsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationStack(
                        onGoogleLoginClick = {
                            launcher.launch(googleSignInClient.signInIntent)
                        },
                        navController = navController,
                        onLogoutClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate(Screens.Login.route){
                                popUpTo(Screens.PexelsList.route){
                                    inclusive = true
                                }

                            }
                        }
                    )
                }
            }
        }
    }
}


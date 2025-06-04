package com.silviomamani.pexelsapp.ui.screens.login

import android.accounts.AccountManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.appevents.ml.ModelManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.silviomamani.pexelsapp.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

import com.google.android.gms.tasks.Task

class LoginScreenViewModel : ViewModel() {

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _showAccountChooser = MutableStateFlow(false)
    val showAccountChooser = _showAccountChooser.asStateFlow()

    private val _availableAccounts = MutableStateFlow<List<GoogleSignInAccount>>(emptyList())
    val availableAccounts = _availableAccounts.asStateFlow()

    private lateinit var googleSignInClient: GoogleSignInClient

    init {
        checkAuthStatus()
    }

    fun initializeGoogleSignIn(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun checkAuthStatus() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            viewModelScope.launch {
                _uiEvent.send("LoginOK")
            }
        }
    }

    fun onGoogleLoginClick(context: Context) {
        viewModelScope.launch {
            try {
                // Verificar si hay cuentas disponibles
                val lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(context)
                val availableAccounts = getAvailableGoogleAccounts(context)

                when {
                    availableAccounts.size > 1 -> {
                        // Mostrar selector de cuentas si hay múltiples cuentas
                        _availableAccounts.value = availableAccounts
                        _showAccountChooser.value = true
                    }
                    lastSignedInAccount != null -> {
                        // Si hay una cuenta previamente logueada, preguntar si usar esa o agregar nueva
                        _availableAccounts.value = listOf(lastSignedInAccount)
                        _showAccountChooser.value = true
                    }
                    else -> {
                        // No hay cuentas, proceder con login normal
                        _uiEvent.send("StartGoogleSignIn")
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send("Error: ${e.message}")
            }
        }
    }

    fun signInWithSelectedAccount(account: GoogleSignInAccount) {
        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val result = FirebaseAuth.getInstance().signInWithCredential(credential).await()

                if (result.user != null) {
                    _showAccountChooser.value = false
                    _uiEvent.send("LoginOK")
                } else {
                    _uiEvent.send("Error: No se pudo iniciar sesión")
                }
            } catch (e: Exception) {
                _uiEvent.send("Error: ${e.message}")
            }
        }
    }

    fun signInWithNewAccount() {
        viewModelScope.launch {
            _showAccountChooser.value = false
            _uiEvent.send("StartGoogleSignIn")
        }
    }

    fun dismissAccountChooser() {
        _showAccountChooser.value = false
    }

    private fun getAvailableGoogleAccounts(context: Context): List<GoogleSignInAccount> {
        return try {
            val accountManager = AccountManager.get(context)
            val accounts = accountManager.getAccountsByType("com.google")

            // Convertir a GoogleSignInAccount (esto es una simplificación)
            // En la práctica, necesitarías obtener los tokens apropiados
            accounts.mapNotNull { account ->
                try {
                    // Aquí deberías obtener el GoogleSignInAccount apropiado
                    GoogleSignIn.getLastSignedInAccount(context)
                } catch (e: Exception) {
                    null
                }
            }.distinctBy { it.email }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = FirebaseAuth.getInstance().signInWithCredential(credential).await()

            if (result.user != null) {
                _uiEvent.send("LoginOK")
            } else {
                _uiEvent.send("Error: No se pudo iniciar sesión")
            }
        } catch (e: ApiException) {
            _uiEvent.send("Error: ${e.message}")
        } catch (e: Exception) {
            _uiEvent.send("Error: ${e.message}")
        }
    }
}
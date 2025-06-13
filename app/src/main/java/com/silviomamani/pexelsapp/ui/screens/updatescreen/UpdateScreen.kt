package com.silviomamani.pexelsapp.ui.screens.updatescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silviomamani.pexelsapp.ui.screens.homescreen.HomeScreenViewModel
import com.silviomamani.pexelsapp.ui.screens.homescreen.Section
import com.silviomamani.pexelsapp.ui.screens.updatescreen.componentsuiupdate.ContactDialog
import com.silviomamani.pexelsapp.ui.screens.updatescreen.componentsuiupdate.UpdateBottomNavigation
import com.silviomamani.pexelsapp.ui.screens.updatescreen.componentsuiupdate.UpdateContactCard
import com.silviomamani.pexelsapp.ui.screens.updatescreen.componentsuiupdate.UpdateScreenHeader
import com.silviomamani.pexelsapp.ui.screens.updatescreen.componentsuiupdate.WhatsAppButton

@Composable
fun UpdateScreen(
    homeViewModel: HomeScreenViewModel,
    updateViewModel: UpdateViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val uiState by updateViewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6C63FF),
                        Color(0xFF3F51B5)
                    )
                )
            )
    ) {
        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UpdateScreenHeader()

            UpdateContactCard(
                onContactClick = { updateViewModel.showContactDialog() }
            )

            WhatsAppButton(
                onClick = { updateViewModel.openWhatsApp(context) },
                isLoading = uiState.isLoading
            )
        }

        // Bottom Navigation
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            UpdateBottomNavigation(
                onHomeClick = onNavigateBack, // ← Navegar de regreso al Home
                onFavoritesClick = { homeViewModel.selectSection(Section.FAVORITES) },
                onUploadClick = { /* Ya estamos en Update */ },
                currentSection = "UPDATE"
            )
        }

        // Dialog de contacto
        ContactDialog(
            isVisible = uiState.showContactDialog,
            onDismiss = { updateViewModel.hideContactDialog() },
            whatsappNumber = uiState.whatsappNumber
        )
    }
}
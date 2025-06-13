package com.silviomamani.pexelsapp.ui.screens.updatescreen.componentsuiupdate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.silviomamani.pexelsapp.ui.screens.commons.BottomNavItem
import com.silviomamani.pexelsapp.ui.screens.homescreen.Section

@Composable
fun UpdateBottomNavigation(
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onUploadClick: () -> Unit,
    currentSection: String = "UPDATE"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                isSelected = currentSection == "HOME",
                onClick = onHomeClick
            )
            BottomNavItem(
                icon = Icons.Default.Favorite,
                label = "Favoritos",
                isSelected = currentSection == "FAVORITES",
                onClick = onFavoritesClick
            )
            BottomNavItem(
                icon = Icons.Default.Upload,
                label = "Subir",
                isSelected = currentSection == "UPDATE",
                onClick = onUploadClick
            )
        }
    }
}
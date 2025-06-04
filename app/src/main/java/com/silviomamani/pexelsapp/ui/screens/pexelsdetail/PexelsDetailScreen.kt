package com.silviomamani.pexelsapp.ui.screens.pexelsdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silviomamani.pexelsapp.ui.screens.commons.PexelsUiItem

@Composable
fun PexelsDetailScreen(
    pexelsId: Int,
    modifier: Modifier = Modifier,
    vm: PexelsDetailScreenViewModel = viewModel()
) {
    vm.setPexelsId(pexelsId)

    if (vm.uiState.pexelsDetail.id == 0) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        PexelsUIItemDetail(
            fotos = vm.uiState.pexelsDetail,
            isFavorito = vm.uiState.isFavorito,
            onToggleFavorito = { vm.toggleFavorito() },
            modifier = modifier
        )
    }
}

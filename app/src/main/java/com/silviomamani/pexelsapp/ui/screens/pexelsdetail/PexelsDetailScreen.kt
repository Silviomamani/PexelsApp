package com.silviomamani.pexelsapp.ui.screens.pexelsdetail


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel


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

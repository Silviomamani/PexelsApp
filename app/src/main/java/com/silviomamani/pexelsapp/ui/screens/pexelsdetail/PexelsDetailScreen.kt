package com.silviomamani.pexelsapp.ui.screens.pexelsdetail


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


@Composable
fun PexelsDetailScreen(
    pexelsId: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    vm: PexelsDetailScreenViewModel = viewModel()
) {
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        vm.initializeDownloadManager(context)
    }

    vm.setPexelsId(pexelsId)

    if (vm.uiState.pexelsDetail.id == 0) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        PexelsUIItemDetail(
            fotos = vm.uiState.pexelsDetail,
            isFavorito = vm.uiState.isFavorito,
            isDownloading = vm.uiState.isDownloading,
            onToggleFavorito = { vm.toggleFavorito() },
            onBackClick = { navController.popBackStack() },
            onDownloadClick = { vm.downloadImage() },
            modifier = modifier ,
            onHomeClick = { navController.navigate("home")}
        )
    }
}
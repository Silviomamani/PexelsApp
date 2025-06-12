package com.silviomamani.pexelsapp.ui.screens.pexelsvideodetail


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun PexelsVideoDetailScreen(
    videoId: Int,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    vm: PexelsVideoDetailViewModel = viewModel()
) {
    val state = vm.uiState

    LaunchedEffect(videoId) {
        vm.fetchVideoById(videoId)
    }

    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}")
            }
        }

        state.video != null -> {
            PexelsVideoUIItemDetail(
                video = state.video,
                onBackClick = { navController.popBackStack() },
                modifier = modifier
            )
        }
    }
}
package com.silviomamani.pexelsapp.ui.screens.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.silviomamani.pexelsapp.ui.screens.Screens
import com.silviomamani.pexelsapp.ui.screens.favorites.componentsui.EmptyFavoritesMessage
import com.silviomamani.pexelsapp.ui.screens.favorites.componentsui.FavoritesPhotoGrid
import com.silviomamani.pexelsapp.ui.screens.favorites.componentsui.FavoritesVideoGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavHostController,
    viewModel: FavoritesScreenViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        val tab = if (pagerState.currentPage == 0) FavoriteTab.PHOTOS else FavoriteTab.VIDEOS
        viewModel.selectTab(tab)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Favoritos",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFE8F5E8)
            )
        )

        // Tab Row
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color(0xFFE8F5E8),
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = { Text("Fotos") }
            )
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = { Text("Videos") }
            )
        }

        // Content
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadFavorites() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            else -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> {
                            if (state.favoritePhotos.isEmpty()) {
                                EmptyFavoritesMessage("No tienes fotos favoritas aún")
                            } else {
                                FavoritesPhotoGrid(
                                    photos = state.favoritePhotos,
                                    onPhotoClick = { photoId ->
                                        navController.navigate("${Screens.PexelsDetail.route}/$photoId")
                                    }
                                )
                            }
                        }
                        1 -> {
                            if (state.favoriteVideos.isEmpty()) {
                                EmptyFavoritesMessage("No tienes videos favoritos aún")
                            } else {
                                FavoritesVideoGrid(
                                    videos = state.favoriteVideos,
                                    onVideoClick = { videoId ->
                                        navController.navigate("${Screens.PexelsVideosDetail.route}/$videoId")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.silviomamani.pexelsapp.ui.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.silviomamani.pexelsapp.ui.screens.Screens
import com.silviomamani.pexelsapp.ui.screens.commons.PexelsItem
import com.silviomamani.pexelsapp.ui.screens.pexelslist.PexelsListScreenViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    userName: String,
    onLogoutClick: () -> Unit,
    photoViewModel: PexelsListScreenViewModel = HomeScreenViewModel(),
    homeViewModel: HomeScreenViewModel = viewModel()
) {
    val state by homeViewModel.state.collectAsState()

    // ✅ Ejecutar según la sección seleccionada
    LaunchedEffect(state.selectedSection) {
        when (state.selectedSection) {
            Section.PHOTOS -> photoViewModel.fetchRecommendedPhotos()
            Section.VIDEOS -> photoViewModel.fetchRecommendedVideos()
            else -> {} // Por ahora nada en FAVORITES o UPLOAD
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
    ) {
        // Header con nombre y logout
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Hola, $userName", fontWeight = FontWeight.Bold)
            Button(onClick = onLogoutClick) { Text("Logout") }
        }

        // Selector de sección
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Section.entries.forEach { section ->
                TextButton(onClick = { homeViewModel.selectSection(section) }) {
                    Text(
                        section.name.lowercase().replaceFirstChar { it.uppercase() },
                        fontWeight = if (section == state.selectedSection) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Título por sección
        Text(
            text = when (state.selectedSection) {
                Section.PHOTOS -> "Fotos recomendadas"
                Section.VIDEOS -> "Videos populares"
                Section.FAVORITES -> "Tus favoritos"
                else -> ""
            },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Contenido
        when {
            state.isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }

            state.errorMessage != null -> {
                Text("Error: ${state.errorMessage}", color = Color.Red)
            }

            state.selectedSection == Section.PHOTOS -> {
                val photos = photoViewModel.recommendedPhotos.collectAsState().value
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(photos) { photo ->
                        PexelsItem.PhotoItem(foto = photo) {
                            navController.navigate(Screens.PexelsDetail.route + "/${photo.id}")
                        }
                    }
                }
            }

            state.selectedSection == Section.VIDEOS -> {
                val videos = photoViewModel.recommendedVideos.collectAsState().value
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(videos) { video ->
                        PexelsItem.VideoItem(video = video) {
                            navController.navigate(Screens.PexelsVideosDetail.route + "/${video.id}")
                        }
                    }
                }
            }

            state.selectedSection == Section.UPLOAD -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = { /* NO HACER NADA */ }) {
                        Text("Subir Foto (No Funciona)")
                    }
                }
            }

            else -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sección aún no implementada.")
                }
            }
        }
    }
}
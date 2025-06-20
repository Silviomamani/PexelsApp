package com.silviomamani.pexelsapp.ui.screens.homescreen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.silviomamani.pexelsapp.ui.screens.Screens
import com.silviomamani.pexelsapp.ui.screens.commons.BottomNavItem
import com.silviomamani.pexelsapp.ui.screens.commons.PhotoCard
import com.silviomamani.pexelsapp.ui.screens.commons.VideoCard
import com.silviomamani.pexelsapp.ui.screens.pexelslist.PexelsListScreenViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    photoViewModel: PexelsListScreenViewModel = viewModel(),
    homeViewModel: HomeScreenViewModel = viewModel()
) {
    val state by homeViewModel.state.collectAsState()
    val context = LocalContext.current

    // Estado para el nombre personalizado
    var displayedName by remember { mutableStateOf("") }

    // Cargar el nombre personalizado desde SharedPreferences
    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE)
        val savedDisplayName = sharedPreferences.getString("display_name", "") ?: ""
        val originalName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Usuario"

        displayedName = if (savedDisplayName.isNotEmpty()) savedDisplayName else originalName
    }

    LaunchedEffect(state.selectedSection) {
        when (state.selectedSection) {
            Section.PHOTOS -> photoViewModel.fetchRecommendedPhotos()
            Section.VIDEOS -> photoViewModel.fetchRecommendedVideos()
            Section.MOSTVIEWED -> photoViewModel.fetchMostViewed()
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE6F0E6),
                            Color(0xFFE6F0E6)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayedName, // Usar el nombre personalizado
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Título principal
            Text(
                text = "Las mejores fotos, imágenes\ny videos compartidas por\ncreadores.",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Barra de búsqueda clickeable
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Navegar a la pantalla de búsqueda
                        navController.navigate(Screens.PexelsList.route)
                    },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Busca fotos gratuitas",
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs de navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Fotos", "Videos", "lo mas visto").forEachIndexed { index, tab ->
                    val isSelected = when (index) {
                        0 -> state.selectedSection == Section.PHOTOS
                        1 -> state.selectedSection == Section.VIDEOS
                        2 -> state.selectedSection == Section.MOSTVIEWED
                        else -> false
                    }

                    Card(
                        modifier = Modifier
                            .clickable {
                                when (index) {
                                    0 -> homeViewModel.selectSection(Section.PHOTOS)
                                    1 -> homeViewModel.selectSection(Section.VIDEOS)
                                    2 -> homeViewModel.selectSection(Section.MOSTVIEWED)
                                }
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color.Black else Color.White.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = tab,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título de sección
            Text(
                text = "Contenido de stock gratuitas",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contenido principal
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                state.errorMessage != null -> {
                    Text("Error: ${state.errorMessage}", color = Color.Red)
                }

                state.selectedSection == Section.PHOTOS -> {
                    val photos = photoViewModel.recommendedPhotos.collectAsState().value
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(photos) { photo ->
                            PhotoCard(
                                photo = photo,
                                onClick = {
                                    navController.navigate(Screens.PexelsDetail.route + "/${photo.id}")
                                }
                            )
                        }
                    }
                }

                state.selectedSection == Section.VIDEOS -> {
                    val videos = photoViewModel.recommendedVideos.collectAsState().value
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(videos) { video ->
                            VideoCard(
                                video = video,
                                onClick = {
                                    navController.navigate(Screens.PexelsVideosDetail.route + "/${video.id}")
                                }
                            )
                        }
                    }
                }
                state.selectedSection == Section.MOSTVIEWED -> {
                    val mostViewed = photoViewModel.mostViewedPhotos.collectAsState().value
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(mostViewed) { photo ->
                            PhotoCard(
                                photo = photo,
                                onClick = {
                                    navController.navigate(Screens.PexelsDetail.route + "/${photo.id}")
                                }
                            )
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sección aún no implementada.", color = Color.White)
                    }
                }
            }
        }

        // Bottom Navigation
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
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
                    isSelected = true,
                    onClick = { }
                )
                BottomNavItem(
                    icon = Icons.Default.Favorite,
                    label = "Favoritos",
                    isSelected = false,
                    onClick = { navController.navigate(Screens.Favorites.route) }
                )
                BottomNavItem(
                    icon = Icons.Default.Person,
                    label = "Perfil",
                    isSelected = false,
                    onClick = { navController.navigate(Screens.Perfil.route) }
                )
            }
        }
    }
}
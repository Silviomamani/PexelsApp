package com.silviomamani.pexelsapp.ui.screens.homescreen

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.silviomamani.pexelsapp.ui.screens.Screens
import com.silviomamani.pexelsapp.ui.screens.commons.PexelsItem
import com.silviomamani.pexelsapp.ui.screens.commons.PexelsUIList
import com.silviomamani.pexelsapp.ui.screens.commons.SearchType
import com.silviomamani.pexelsapp.ui.screens.pexelslist.PexelsListScreenViewModel


@Composable
fun HomeScreen(
    navController: NavHostController,
    userName: String,
    onLogoutClick: () -> Unit,
    photoViewModel: PexelsListScreenViewModel = viewModel(),
    homeViewModel: HomeScreenViewModel = viewModel()
) {
    val state by homeViewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var searchType by remember { mutableStateOf(SearchType.PHOTOS) }
    var expanded by remember { mutableStateOf(false) }


    LaunchedEffect(state.selectedSection) {
        when (state.selectedSection) {
            Section.PHOTOS -> photoViewModel.fetchRecommendedPhotos()
            Section.VIDEOS -> photoViewModel.fetchRecommendedVideos()
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Hola, $userName", fontWeight = FontWeight.Bold)
            Button(onClick = onLogoutClick) { Text("Logout") }
        }


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


        if (state.selectedSection == Section.PHOTOS || state.selectedSection == Section.VIDEOS) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Buscar ${searchType.name.lowercase().replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                Box {
                    Button(onClick = { expanded = true }) {
                        Text(searchType.name)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("FOTOS") },
                            onClick = {
                                searchType = SearchType.PHOTOS
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("VIDEOS") },
                            onClick = {
                                searchType = SearchType.VIDEOS
                                expanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))


                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Buscar ${searchType.name.lowercase()}") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(12.dp))


                Button(
                    onClick = {

                        photoViewModel.onSearchTypeChange(searchType)
                        photoViewModel.searchChange(searchQuery)
                        photoViewModel.fetchResults()
                    }
                ) {
                    Text("Buscar")
                }
            }
        }


        if (state.selectedSection == Section.FAVORITES || state.selectedSection == Section.UPLOAD) {
            Text(
                text = when (state.selectedSection) {
                    Section.FAVORITES -> "Tus favoritos"
                    Section.UPLOAD -> "Subir contenido"
                    else -> ""
                },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }


        when {
            state.isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }

            state.errorMessage != null -> {
                Text("Error: ${state.errorMessage}", color = Color.Red)
            }

            state.selectedSection == Section.PHOTOS -> {

                if (searchQuery.isNotEmpty() && photoViewModel.uiState.searchQuery.isNotEmpty()) {

                    PexelsUIList(
                        pexelsList = photoViewModel.combinedList,
                        modifier = Modifier.fillMaxSize(),
                        onClick = { id, isVideo ->
                            if (isVideo) {
                                navController.navigate(Screens.PexelsVideosDetail.route + "/$id")
                            } else {
                                navController.navigate(Screens.PexelsDetail.route + "/$id")
                            }
                        }
                    )
                } else {

                    val photos = photoViewModel.recommendedPhotos.collectAsState().value
                    val photoItems = photos.map { PexelsItem.PhotoItem(foto = it) }
                    PexelsUIList(
                        pexelsList = photoItems,
                        modifier = Modifier.fillMaxSize(),
                        onClick = { id, isVideo ->
                            navController.navigate(Screens.PexelsDetail.route + "/$id")
                        }
                    )
                }
            }

            state.selectedSection == Section.VIDEOS -> {

                if (searchQuery.isNotEmpty() && photoViewModel.uiState.searchQuery.isNotEmpty()) {

                    PexelsUIList(
                        pexelsList = photoViewModel.combinedList,
                        modifier = Modifier.fillMaxSize(),
                        onClick = { id, isVideo ->
                            if (isVideo) {
                                navController.navigate(Screens.PexelsVideosDetail.route + "/$id")
                            } else {
                                navController.navigate(Screens.PexelsDetail.route + "/$id")
                            }
                        }
                    )
                } else {

                    val videos = photoViewModel.recommendedVideos.collectAsState().value
                    val videoItems = videos.map { PexelsItem.VideoItem(video = it) }
                    PexelsUIList(
                        pexelsList = videoItems,
                        modifier = Modifier.fillMaxSize(),
                        onClick = { id, isVideo ->
                            navController.navigate(Screens.PexelsVideosDetail.route + "/$id")
                        }
                    )
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

package com.silviomamani.pexelsapp.ui.screens.pexelsdetail


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Upload

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.silviomamani.pexelsapp.photos.Fotos
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.silviomamani.pexelsapp.ui.screens.commons.BottomNavItem
import com.silviomamani.pexelsapp.ui.screens.homescreen.Section


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PexelsUIItemDetail(
    fotos: Fotos,
    isFavorito: Boolean,
    isDownloading: Boolean,
    onToggleFavorito: () -> Unit,
    onBackClick: () -> Unit,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit
) {

    Scaffold(
        containerColor = Color(0xFFE4F5E4),
        bottomBar = {
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
                        isSelected = true,
                        onClick = { }
                    )
                    BottomNavItem(
                        icon = Icons.Default.Favorite,
                        label = "Favoritos",
                        isSelected = false,
                        onClick = { }
                    )
                    BottomNavItem(
                        icon = Icons.Default.Upload,
                        label = "Subir",
                        isSelected = false,
                        onClick = { }
                    )
                }
            }



        },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color(0xFF4A6741)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE4F5E4)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(Color(0xFFE4F5E4)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(280.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = fotos.src.medium,
                    contentDescription = fotos.alt,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )

                IconButton(
                    onClick = onToggleFavorito,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(
                            Color.Black.copy(alpha = 0.3f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = if (isFavorito) Color.Red else Color.White
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fotos.alt.ifEmpty { "Lorem" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF4A6741),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${fotos.width} x ${fotos.height}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B7B63)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                AsyncImage(
                    model = "https://randomuser.me/api/portraits/women/1.jpg",
                    contentDescription = "Fotógrafo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFB8D4B8), CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = fotos.photographer,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF4A6741)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onDownloadClick,
                enabled = !isDownloading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7BA839),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (isDownloading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Descargando...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Text(
                        text = "Descargar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
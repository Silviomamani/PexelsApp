package com.silviomamani.pexelsapp.ui.screens.pexelsdetail

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PexelsUIItemDetail(
    fotos: Fotos,
    isFavorito: Boolean,
    onToggleFavorito: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    Scaffold(
        containerColor = Color(0xFFE4F5E4),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFE4F5E4),
                contentColor = Color(0xFF4A6741)
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.Place,
                            contentDescription = "Inicio",
                            tint = Color(0xFF4A6741)
                        )
                    },
                    label = {
                        Text(
                            "Inicio",
                            color = Color(0xFF4A6741)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4A6741),
                        unselectedIconColor = Color(0xFF4A6741),
                        selectedTextColor = Color(0xFF4A6741),
                        unselectedTextColor = Color(0xFF4A6741),
                        indicatorColor = Color(0xFFB8D4B8)
                    )
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = "Favoritos",
                            tint = Color(0xFF4A6741)
                        )
                    },
                    label = {
                        Text(
                            "Favoritos",
                            color = Color(0xFF4A6741)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4A6741),
                        unselectedIconColor = Color(0xFF4A6741),
                        selectedTextColor = Color(0xFF4A6741),
                        unselectedTextColor = Color(0xFF4A6741),
                        indicatorColor = Color(0xFFB8D4B8)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.CloudUpload,
                            contentDescription = "Subir",
                            tint = Color(0xFF4A6741)
                        )
                    },
                    label = {
                        Text(
                            "Subir",
                            color = Color(0xFF4A6741)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4A6741),
                        unselectedIconColor = Color(0xFF4A6741),
                        selectedTextColor = Color(0xFF4A6741),
                        unselectedTextColor = Color(0xFF4A6741),
                        indicatorColor = Color(0xFFB8D4B8)
                    )
                )
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
                    text = "Medidas",
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
                onClick = {
                    // Usar la URL de mayor calidad disponible para descargar
                    val downloadUrl = fotos.src.original
                    val uri = Uri.parse(downloadUrl)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                },
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
                Text(
                    text = "Descargar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
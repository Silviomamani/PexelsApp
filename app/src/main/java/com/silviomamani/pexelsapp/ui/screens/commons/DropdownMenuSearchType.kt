package com.silviomamani.pexelsapp.ui.screens.commons

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun DropdownMenuSearchType(
    selectedType: SearchType,
    onTypeSelected: (SearchType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(text = selectedType.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Fotos") }, onClick = {
                onTypeSelected(SearchType.PHOTOS)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Videos") }, onClick = {
                onTypeSelected(SearchType.VIDEOS)
                expanded = false
            })
        }
    }
}
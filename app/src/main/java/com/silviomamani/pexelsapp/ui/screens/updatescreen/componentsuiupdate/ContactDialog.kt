package com.silviomamani.pexelsapp.ui.screens.updatescreen.componentsuiupdate

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ContactDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    whatsappNumber: String
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("Contacto WhatsApp")
            },
            text = {
                Text("WhatsApp no está disponible. Puedes contactar a @silvio.mm al número: $whatsappNumber")
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Entendido")
                }
            }
        )
    }
}
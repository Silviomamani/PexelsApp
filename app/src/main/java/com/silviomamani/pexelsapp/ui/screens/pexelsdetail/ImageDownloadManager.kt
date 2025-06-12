package com.silviomamani.pexelsapp.ui.screens.pexelsdetail

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImageDownloadManager(private val context: Context) {

    private val imageLoader = ImageLoader(context)

    suspend fun downloadImage(
        imageUrl: String,
        fileName: String = "pexels_image_${System.currentTimeMillis()}.jpg"
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Descargar la imagen usando Coil
                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .build()

                val drawable = imageLoader.execute(request).drawable
                val bitmap = (drawable as? BitmapDrawable)?.bitmap

                if (bitmap != null) {
                    saveImageToGallery(bitmap, fileName)
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
                    }
                    false
                }
            } catch (e: Exception) {
                Log.e("ImageDownload", "Error al descargar imagen: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al descargar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                false
            }
        }
    }

    private suspend fun saveImageToGallery(bitmap: Bitmap, fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val saved = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveImageToGalleryApi29AndAbove(bitmap, fileName)
                } else {
                    saveImageToGalleryLegacy(bitmap, fileName)
                }

                withContext(Dispatchers.Main) {
                    if (saved) {
                        Toast.makeText(context, "Imagen guardada en la galería", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
                    }
                }

                saved
            } catch (e: Exception) {
                Log.e("ImageDownload", "Error al guardar imagen: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                false
            }
        }
    }

    private fun saveImageToGalleryApi29AndAbove(bitmap: Bitmap, fileName: String): Boolean {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Pexels")
        }

        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        return uri?.let {
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                true
            } ?: false
        } ?: false
    }

    @Suppress("DEPRECATION")
    private fun saveImageToGalleryLegacy(bitmap: Bitmap, fileName: String): Boolean {
        val imagesDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "Pexels"
        )

        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }

        val image = File(imagesDir, fileName)

        return try {
            val outputStream = FileOutputStream(image)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()

            // Notificar al MediaScanner para que la imagen aparezca en la galería
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                image.absolutePath,
                fileName,
                "Imagen descargada de Pexels"
            )

            true
        } catch (e: Exception) {
            Log.e("ImageDownload", "Error en saveImageToGalleryLegacy: ${e.message}")
            false
        }
    }
}
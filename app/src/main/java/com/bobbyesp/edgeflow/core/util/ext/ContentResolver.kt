package com.bobbyesp.edgeflow.core.util.ext

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.bobbyesp.edgeflow.R
import com.bobbyesp.edgeflow.core.util.exceptions.OversizeFileException
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * Reads the bytes from a given URI.
 *
 * @param uri The URI of the file to read.
 * @param maxAllowedSize The maximum allowed size of the file in bytes. Defaults to 32 MB.
 * @return A byte array containing the file's data, or null if the file could not be read.
 * @throws OversizeFileException If the file size exceeds the maximum allowed size.
 * @throws Exception If the file could not be opened.
 */
@Throws(OversizeFileException::class, Exception::class)
fun ContentResolver.readBytesFromUri(
    context: Context,
    uri: Uri,
    maxAllowedSize: Long = 32.MB
): ByteArray? {
    val fileSize = getFileSize(uri)

    if (fileSize > maxAllowedSize) {
        throw OversizeFileException(
            context.getString(
                R.string.max_filesize_exceeded,
                (maxAllowedSize.toString())
            )
        )
    }

    val inputStream: InputStream =
        openInputStream(uri) ?: throw Exception(context.getString(R.string.error_opening_file))
    val byteArrayOutputStream = ByteArrayOutputStream()

    val buffer = ByteArray(1024)
    var bytesRead: Int
    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
        byteArrayOutputStream.write(buffer, 0, bytesRead)
    }

    inputStream.close()
    return byteArrayOutputStream.toByteArray()
}

/**
 * Retrieves the size of the file located at the given URI.
 *
 * @param uri The URI of the file whose size is to be determined.
 * @return The size of the file in bytes.
 * @throws Exception If the file size cannot be determined.
 */
fun ContentResolver.getFileSize(uri: Uri): Long {
    query(uri, arrayOf(android.provider.OpenableColumns.SIZE), null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
            if (sizeIndex != -1) {
                return cursor.getLong(sizeIndex)
            }
        }
    }
    throw Exception("Unable to determine file size")
}
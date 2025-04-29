package com.bobbyesp.edgeflow.util

import java.io.File
import java.security.MessageDigest

object Hashing {
    /**
     * Generates a SHA-256 hash for the given file.
     *
     * @param file The file to generate the hash for.
     * @return The SHA-256 hash of the file as a hexadecimal string.
     */
    fun generateFileHash(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { inputStream ->
            val buffer = ByteArray(1024) // 1 KB buffer
            var bytesRead: Int // Number of bytes read
            while (inputStream.read(buffer).also { bytesRead = it } != -1) { // Read until EOF
                digest.update(buffer, 0, bytesRead) // Update the digest with the read bytes
            }
        }
        return digest.digest().joinToString(separator = "") { "%02x".format(it) }
    }
}
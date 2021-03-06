package com.blokkok.mod.sab.managers

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * This object class stores information about common files such as testkey
 * and is also responsible for extracting them
 */
object CommonFilesManager {

    private lateinit var filesDir: File
    private lateinit var testKeyDir: File

    val testKeyPrivateKey: File by lazy { testKeyDir.resolve("testkey.pk8") }
    val testKeyPublicKey: File  by lazy { testKeyDir.resolve("testkey.x509.pem") }

    fun initialize(context: Context, assetsFolder: File) {
        filesDir = context.filesDir
        testKeyDir = File(filesDir, "testkey")

        if (!testKeyDir.exists()) {
            // Extract testKey
            testKeyDir.mkdirs()
            extractTestKey(assetsFolder)
        }
    }

    private fun extractTestKey(assets: File) {
        unpackZip(
            ZipInputStream(
                FileInputStream(
                    assets.resolve("testkey.zip")
                )
            ),
            testKeyDir
        )
    }
}

// Used to unpack zip files into a specified output path
fun unpackZip(
    zipInputStream: ZipInputStream,
    outputPath: File,
): Boolean {
    try {
        var filename: String

        var entry: ZipEntry?
        val buffer = ByteArray(1024)
        var count: Int

        while (zipInputStream.nextEntry.also { entry = it } != null) {
            filename = entry!!.name

            if (entry!!.isDirectory) {
                File(outputPath, filename).mkdirs()
                continue
            }

            val fileOut = FileOutputStream(File(outputPath, filename))

            while (zipInputStream.read(buffer).also { count = it } != -1)
                fileOut.write(buffer, 0, count)

            fileOut.close()
            zipInputStream.closeEntry()
        }

        zipInputStream.close()

    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }

    return true
}
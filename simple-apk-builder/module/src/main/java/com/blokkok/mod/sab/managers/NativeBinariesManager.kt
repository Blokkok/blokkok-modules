package com.blokkok.mod.sab.managers

import android.content.Context
import java.io.*
import java.util.zip.ZipInputStream

// Use the default extraction to home folder + execute it there
const val useLegacyMethod = true

object NativeBinariesManager {

    private lateinit var nativeLibraryDir: File
    private lateinit var dataDir: File
    private lateinit var binariesDir: File

    /**
     * Used to indicate whether do we need to extract the binaries or not.
     * Call NativeBinariesManager#executeBinaries(Context) if this value is true
     */

    fun initialize(context: Context, assetsFolder: File) {
        nativeLibraryDir = File(context.applicationInfo.nativeLibraryDir)
        dataDir = File(context.applicationInfo.dataDir)
        binariesDir = File(dataDir, "binaries")

        if (useLegacyMethod) {
            // Check if the binaries are already extracted
            if (!binariesDir.exists()) {
                // Does not seem so, then extract the binaries
                binariesDir.mkdir()
                extractBinaries(assetsFolder)
            }

        } else {
            throw NotImplementedError("Non-legacy binary execution method is not implemented yet")
        }
    }

    private fun extractBinaries(assets: File) {
        Thread {
            /* The structure of the binaries.zip would be
             *
             * aapt2
             * L bin
             * | L aapt2
             * L lib
             *   L (aapt2's libraries)
             *
             * zipalign
             * L bin
             * | L zipalign
             */
            unpackZip(
                ZipInputStream(
                    FileInputStream(
                        assets.resolve("binaries.zip")
                    )
                ),
                binariesDir
            )

            // Then make them executable
            NativeBinaries.values().forEach {
                File(getBinaryPath(it)).setExecutable(true)
            }
        }.run()
    }

    fun executeCommand(
        binary: NativeBinaries,
        arguments: Array<String>,
        stdout: (String) -> Unit,
        stderr: (String) -> Unit,
    ): Int {
        val process = Runtime.getRuntime().exec(
            ArrayList<String>().apply {
                add(getBinaryPath(binary))
                addAll(arguments)
            }.toTypedArray()
        )

        process.inputStream.redirectTo(stdout)
        process.errorStream.redirectTo(stderr)

        process.waitFor()

        return process.exitValue()
    }

    private fun getBinaryPath(binary: NativeBinaries): String {
        if (useLegacyMethod) {
            return when (binary) {
                NativeBinaries.AAPT2 -> File(binariesDir, "aapt2/bin/aapt2").absolutePath
                NativeBinaries.ZIP_ALIGN -> File(binariesDir, "zipalign/bin/zipalign").absolutePath
            }
        } else {
            throw NotImplementedError("Non-legacy binary execution method is not implemented yet")
        }
    }

    enum class NativeBinaries {
        AAPT2,
        ZIP_ALIGN,
    }
}

private fun InputStream.redirectTo(out: (String) -> Unit) {
    Thread {
        BufferedReader(InputStreamReader(this)).also { reader ->
            reader.forEachLine { out(it) }
        }.close()
    }.run()
}
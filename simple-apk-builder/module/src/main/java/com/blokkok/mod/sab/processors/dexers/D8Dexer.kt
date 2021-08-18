package com.blokkok.mod.sab.processors.dexers

import com.blokkok.mod.sab.AssetsPaths
import com.blokkok.mod.sab.processors.Dexer
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

object D8Dexer : Dexer {

    override val name: String get() = "D8"

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun dex(
        folderOrFile: File,
        output: File,
        stdout: (String) -> Unit,
        stderr: (String) -> Unit,
        libraries: Array<File>?,
    ): Int {

        val classpath = StringBuilder().apply {
            libraries?.forEach {
                append("--classpath ")
                append(it.absolutePath)
            }
        }

        val mergeLibraries = libraries?.joinToString(" ") ?: ""

        val process = Runtime.getRuntime().exec(
            "dalvikvm -Xmx256m -cp ${AssetsPaths.d8Jar.absolutePath} com.android.tools.r8.D8 --release --classpath ${AssetsPaths.androidJar.absolutePath} $classpath --output ${output.absolutePath} ${listFiles(folderOrFile).joinToString(" ")} $mergeLibraries"
        )

        process.inputStream.redirectTo(stdout)
        process.errorStream.redirectTo(stderr)

        process.waitFor()

        return process.exitValue()
    }
}

private fun InputStream.redirectTo(out: (String) -> Unit) {
    Thread {
        BufferedReader(InputStreamReader(this)).also { reader ->
            reader.forEachLine { out(it) }
        }.close()
    }.run()
}

private fun listFiles(folder: File): List<String> {
    if (!folder.exists()) return emptyList()
    if (folder.isFile) return listOf(folder.absolutePath)

    return ArrayList<String>().apply {
        folder.listFiles()!!.forEach { file ->
            if (file.isDirectory) {
                addAll(listFiles(file))
            } else {
                add(file.absolutePath)
            }
        }
    }
}
package com.blokkok.mod.sab.processors.compilers

import com.blokkok.mod.sab.AssetsPaths
import com.blokkok.mod.sab.processors.JavaCompiler
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

// TODO: 7/16/21 Move android.jar extraction to somewhere else

object ECJCompiler : JavaCompiler {

    override val name: String get() = "ECJ"

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun compileJava(
        inputFolders: Array<File>,
        outputFolder: File,
        stdout: (String) -> Unit,
        stderr: (String) -> Unit,
        classpaths: Array<File>?,
    ): Int {

        val classpathsRendered = StringBuilder().apply {
            classpaths?.forEach {
                append(":")
                append(it.absolutePath)
            }
        }

        val process = Runtime.getRuntime().exec(
            "dalvikvm -Xmx256m -Xcompiler-option --compiler-filter=speed -cp ${AssetsPaths.ecjJar.absolutePath} org.eclipse.jdt.internal.compiler.batch.Main -proc:none -7 -cp ${AssetsPaths.androidJar.absolutePath}$classpathsRendered ${inputFolders.joinToString(" ")} -verbose -d ${outputFolder.absolutePath}"
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
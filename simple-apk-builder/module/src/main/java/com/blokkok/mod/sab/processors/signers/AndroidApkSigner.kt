package com.blokkok.mod.sab.processors.signers

import com.blokkok.mod.sab.AssetsPaths
import com.blokkok.mod.sab.processors.ApkSigner
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

object AndroidApkSigner : ApkSigner {
    override val name: String = "ApkSigner"

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun sign(
        apkFile: File,
        outputApk: File,
        privateKey: File,
        publicKey: File,
        stdout: (String) -> Unit,
        stderr: (String) -> Unit
    ): Int {
        val process = Runtime.getRuntime().exec(
            "dalvikvm -Xmx256m -cp ${AssetsPaths.apksignerJar.absolutePath} com.android.apksigner.ApkSignerTool sign --in ${apkFile.absolutePath} --out ${outputApk.absolutePath} --key ${privateKey.absolutePath} --cert ${publicKey.absolutePath} -v"
        )

        process.inputStream.redirectTo(stdout)
        process.errorStream.redirectTo(stderr)

        return process.waitFor()
    }
}

private fun InputStream.redirectTo(out: (String) -> Unit) {
    Thread {
        BufferedReader(InputStreamReader(this)).also { reader ->
            reader.forEachLine { out(it) }
        }.close()
    }.run()
}
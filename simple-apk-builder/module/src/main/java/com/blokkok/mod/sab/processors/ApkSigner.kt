package com.blokkok.mod.sab.processors

import java.io.File

interface ApkSigner {
    val name: String

    suspend fun sign(
        apkFile: File,
        outputApk: File,
        privateKey: File,
        publicKey: File,
        stdout: (String) -> Unit,
        stderr: (String) -> Unit,
    ): Int // return value
}
package com.blokkok.mod.sab.processors

import java.io.File

interface Dexer {
    val name: String

    suspend fun dex(
        folderOrFile: File,
        output: File,
        stdout: (String) -> Unit,
        stderr: (String) -> Unit,
        libraries: Array<File>? = null,
    ): Int // return value
}
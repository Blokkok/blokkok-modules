package com.blokkok.mod.sab.processors

import java.io.File

interface JavaCompiler {
    val name: String

    suspend fun compileJava(
        inputFolders: Array<File>,
        outputFolder: File,
        stdout: (String) -> Unit,
        stderr: (String) -> Unit,
        classpaths: Array<File>? = null,
    ): Int // return value
}
package com.blokkok.mod.sab.processors

import java.io.File
import java.io.IOException

interface JavaCompiler {
    val name: String

    @Throws(IOException::class)
    fun compileJava(
        inputFolders: Array<File>,
        outputFolder: File,
        stdout: (String) -> Unit,
        stderr: (String) -> Unit,
        classpaths: Array<File>? = null,
    ): Int // return value
}
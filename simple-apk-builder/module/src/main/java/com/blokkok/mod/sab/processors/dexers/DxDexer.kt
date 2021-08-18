package com.blokkok.mod.sab.processors.dexers

import android.content.Context
import com.blokkok.mod.sab.processors.Dexer
import java.io.File

object DxDexer : Dexer {
    override val name: String get() = "Dx"

    override fun initialize(context: Context, assetsFolder: File) {
        TODO("Dx is not yet implemented")
    }

    override suspend fun dex(
        folderOrFile: File,
        output: File,
        stdout: (String) -> Unit,
        stderr: (String) -> Unit,
        classpaths: Array<File>?
    ): Int {
        TODO("Dx is not yet implemented")
    }
}
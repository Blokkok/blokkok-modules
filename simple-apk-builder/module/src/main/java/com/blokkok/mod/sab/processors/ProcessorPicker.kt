package com.blokkok.mod.sab.processors

import android.os.Build
import com.blokkok.mod.sab.processors.compilers.ECJCompiler
import com.blokkok.mod.sab.processors.dexers.D8Dexer
import com.blokkok.mod.sab.processors.dexers.DxDexer
import com.blokkok.mod.sab.processors.signers.AndroidApkSigner

object ProcessorPicker {
    fun pickDexer(): Dexer {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> D8Dexer
            else -> DxDexer
        }
    }

    fun pickCompiler(): JavaCompiler = ECJCompiler

    fun pickSigner(): ApkSigner = AndroidApkSigner
}
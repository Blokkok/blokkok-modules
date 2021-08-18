package com.blokkok.mod.sab

import com.blokkok.mod.sab.processors.ProcessorPicker
import java.io.File
import java.util.concurrent.Executors

object ApkBuilder {
    private val compileExecutor = Executors.newSingleThreadExecutor()

    fun doCompilation(
        cacheDir: File,
        manifestFile: File,
        javaSrc: File,
        resFolder: File,
        outputApk: File,
        logOut: (String) -> Unit,
        updateStatus: (BuildStatus) -> Unit
    ) {
        compileExecutor.submit {
            compile(updateStatus)
        }
    }

    private fun compile(updateStatus: (BuildStatus) -> Unit) {
        // pick processors
        val compiler = ProcessorPicker.pickCompiler()
        val dexer = ProcessorPicker.pickDexer()
        val signer = ProcessorPicker.pickSigner()

        // TODO: 8/18/21 do these parts
    }

    /**
     * Represents the build status
     */
    sealed class BuildStatus {
        object CompilingRes
        object LinkingRes
        object CompilingJavaSrc
        object Dexifying
        object BuildingApk

        data class Failure(val text: String)
        object Success
    }
}
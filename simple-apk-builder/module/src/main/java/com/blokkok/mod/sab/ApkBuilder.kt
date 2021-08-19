package com.blokkok.mod.sab

import com.blokkok.mod.sab.managers.NativeBinariesManager
import com.blokkok.mod.sab.processors.ProcessorPicker
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.properties.Delegates

// TODO: 8/19/21 implement libraries
class ApkBuilder(
    private val cacheDir: File,
    private val manifestFile: File,
    private val javaSrc: File,
    private val resFolder: File,
    private val outputApk: File,
    private val logOut: (String) -> Unit,
    private val updateStatus: (BuildStatus) -> Unit,
) {
    // cache locations
    private val resZipOutput = cacheDir.resolve("res.zip")
    private val javaClasses = cacheDir.resolve("classes")

    private val javaGen by lazy {
        cacheDir.resolve("gen").also { it.mkdirs() }
    }

    private val compileExecutor = Executors.newSingleThreadExecutor()
    private var status: BuildStatus by Delegates.observable(BuildStatus.Idle) { _, _, status_ ->
        updateStatus(status_)
        logOut("Status changed to ${status_.displayText}")
    }

    fun performCompilation() {
        updateStatus(BuildStatus.Initializing)

        compileExecutor.submit {
            compile(updateStatus)
        }
    }

    private fun compile(updateStatus: (BuildStatus) -> Unit) {
        // pick processors
        val compiler = ProcessorPicker.pickCompiler()
        val dexer = ProcessorPicker.pickDexer()
        val signer = ProcessorPicker.pickSigner()

        val exec = ExecImpl()

        // alright let's compile the resources of this app
        exec.compileResWithAAPT2(resFolder)
    }

    /**
     * Where stuff like calling the included binaries are happening. This is here to encapsulate
     * the program execution code from the app builder logic.
     */
    private inner class ExecImpl {
        /**
         * Compiles the res folder into a compiled resources bundled as a zip file using AAPT2
         */
        @Throws(IOException::class)
        fun compileResWithAAPT2(resFolder: File): Int {
            updateStatus(BuildStatus.CompilingRes)
            logOut("Compiling resources with AAPT2")

            return NativeBinariesManager.executeCommand(
                NativeBinariesManager.NativeBinaries.AAPT2,
                arrayOf(
                    "compile",
                    "--dir", resFolder.absolutePath,
                    "-o", resZipOutput.absolutePath,
                    "-v" // verbose boi
                ),
                { logOut("AAPT2 >> $it") },
                { logOut("AAPT2 >> $it") }, // aapt2 for some reason output it's logs into stderr
            )
        }
    }
}

/**
 * Represents the build status
 */
sealed class BuildStatus {
    open val displayText = ""

    object Idle : BuildStatus() {
        override val displayText = "IDLE"
    }

    object Initializing : BuildStatus() {
        override val displayText = "Initializing"
    }

    object CompilingRes : BuildStatus() {
        override val displayText = "Compiling resources"
    }

    object LinkingRes : BuildStatus() {
        override val displayText = "Linking resources"
    }

    object CompilingJavaSrc : BuildStatus() {
        override val displayText = "Compiling java sources"
    }

    object Dexifying : BuildStatus() {
        override val displayText = "Dexifying compiled classes"
    }

    object BuildingApk : BuildStatus() {
        override val displayText = "Builing APK"
    }

    data class Failure(val text: String) : BuildStatus() {
        override val displayText = "Failure on build: $text"
    }

    object Success : BuildStatus() {
        override val displayText = "Success"
    }
}

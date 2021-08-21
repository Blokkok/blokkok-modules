package com.blokkok.mod.sab

import com.android.sdklib.build.ApkBuilder
import com.blokkok.mod.sab.managers.CommonFilesManager
import com.blokkok.mod.sab.managers.NativeBinariesManager
import com.blokkok.mod.sab.processors.ApkSigner
import com.blokkok.mod.sab.processors.Dexer
import com.blokkok.mod.sab.processors.JavaCompiler
import com.blokkok.mod.sab.processors.ProcessorPicker
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.io.PrintStream
import java.security.PrivateKey
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

    private val compiledClasses by lazy {
        cacheDir.resolve("classes").also { it.mkdirs() }
    }

    private val compiledDexes by lazy {
        cacheDir.resolve("dex").also { it.mkdirs() }
    }

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
            compile()
        }
    }

    private fun compile() {
        kotlin.runCatching {
            // pick processors
            val compiler = ProcessorPicker.pickCompiler()
            val dexer = ProcessorPicker.pickDexer()
            val signer = ProcessorPicker.pickSigner()

            val exec = ExecImpl()

            // alright let's compile the resources of this app
            exec.compileResWithAAPT2(resFolder)

            // then link them
            exec.linkResWithAAPT2(AssetsPaths.androidJar,
                resZipOutput,
                manifestFile,
                javaGen,
                outputApk)

            // compile java sources
            exec.compileJava(
                compiler,
                listOf(javaSrc, javaGen),
                compiledClasses
            )

            // dex classes
            exec.dexClasses(
                dexer,
                compiledClasses,
                compiledDexes
            )

            // Build the APK
            exec.buildApk(
                compiledDexes,
                resZipOutput,
                outputApk
            )

            // TODO: 8/21/21 zipalign is not necessary but ok

            // Sign the apk!
            exec.signApk(
                signer,

                outputApk,
                outputApk,

                CommonFilesManager.testKeyPublicKey,
                CommonFilesManager.testKeyPrivateKey,
            )

            // return Unit
            return@runCatching
        }.onFailure { exception ->
            val message = exception.message

            status = message?.let { BuildStatus.Failure(it) }
                ?: BuildStatus.Failure(exception.toString())

            logOut("Build failed with exception: \n${exception.stackTraceToString()}")

        }.onSuccess {
            // yay!
            status = BuildStatus.Success
            logOut("Build Successful! APK file is saved at: ${outputApk.absolutePath}")
        }
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
            status = BuildStatus.CompilingRes
            logOut("Compiling resources with AAPT2")

            return NativeBinariesManager.executeCommand(
                NativeBinariesManager.NativeBinaries.AAPT2,
                arrayOf(
                    "compile",
                    "--dir", resFolder.absolutePath,
                    "-o", resZipOutput.absolutePath,
                    "-v" // verbose boi
                ),
                { logOut("AAPT2 C >> $it") },
                { logOut("AAPT2 C >> $it") }, // aapt2 for some reason output it's logs into stderr
            )
        }

        @Throws(IOException::class)
        fun linkResWithAAPT2(
            androidJar: File,
            compiledResZip: File,
            manifestFile: File,
            javaGen: File,
            outputApk: File,
        ): Int {
            status = BuildStatus.LinkingRes
            logOut("Linking resources with AAPT2")

            return NativeBinariesManager.executeCommand(
                NativeBinariesManager.NativeBinaries.AAPT2,
                arrayOf(
                    "link",
                    "-I", androidJar.absolutePath,
                    "--auto-add-overlay",
                    "--allow-reserved-package-id",
                    "--no-version-vectors",
                    "--min-sdk-version", "21", // TODO: 7/20/21 add these to the project metadata
                    "--target-sdk-version", "30",
                    "--version-code", "1",
                    "--version-name", "1.0",
                    "--manifest", manifestFile.absolutePath,
                    "--java", javaGen.absolutePath,
                    "-o", outputApk.absolutePath,
                    compiledResZip.absolutePath,
                    "-v"
                ),
                { logOut("AAPT2 L >> $it") },
                { logOut("AAPT2 L >> $it") }, // aapt2 for some reason output it's logs into stderr
            )
        }

        @Throws(IOException::class)
        fun compileJava(
            compiler: JavaCompiler,
            sources: List<File>,
            compiledClasses: File,
        ): Int {
            status = BuildStatus.CompilingJavaSrc
            logOut("${compiler.name} is compiling java sources")

            return compiler.compileJava(
                sources.toTypedArray(),
                compiledClasses,
                { logOut("${compiler.name} >> ") },
                { logOut("${compiler.name} ERR >> ") },
            )
        }

        @Throws(IOException::class)
        fun dexClasses(
            dexer: Dexer,
            classesFolder: File,
            outputFolder: File,
        ): Int {
            status = BuildStatus.Dexifying
            logOut("${dexer.name} is dexifying classes")

            return dexer.dex(
                classesFolder, outputFolder,
                { logOut("${dexer.name} >> $it") },
                { logOut("${dexer.name} ERR >> $it") },
            )
        }

        fun buildApk(
            folderOfDexes: File,
            resZip: File,
            outputApk: File,
        ) {
            status = BuildStatus.BuildingApk
            logOut("ApkBuilder is building the APK")

            val apkBuilder = ApkBuilder(
                outputApk,
                resZip,
                null,
                null,
                null,
                PrintStream(OutputStreamLogger("ApkBuilder >> "))
            )

            // add the dexes
            for (dex in folderOfDexes.listFiles()!!) {
                apkBuilder.addFile(dex, dex.name)
            }

            // then seal it
            apkBuilder.setDebugMode(false)
            apkBuilder.sealApk()
        }

        @Throws(IOException::class)
        fun signApk(
            signer: ApkSigner,

            inputApk: File,
            outputApk: File,

            keyPublicKey: File,
            keyPrivateKey: File,
        ): Int {
            status = BuildStatus.SigningApk
            logOut("Signing apk with ${signer.name}")

            return signer.sign(
                inputApk,
                outputApk,
                keyPrivateKey,
                keyPublicKey,
                { logOut("${signer.name} >> ") },
                { logOut("${signer.name} ERR >> ") },
            )
        }

        inner class OutputStreamLogger(
            private val prefix: String
        ) : OutputStream() {

            private val buffer = StringBuilder()

            override fun write(b: Int) {
                val char = b.toChar()

                if (char == '\n') {
                    logOut("$prefix$buffer")
                    buffer.clear()
                } else {
                    buffer.append(char)
                }
            }
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
        override val displayText = "Building APK"
    }

    object SigningApk : BuildStatus() {
        override val displayText: String = "Signing APK"
    }

    data class Failure(val text: String) : BuildStatus() {
        override val displayText = "Failure on build: $text"
    }

    object Success : BuildStatus() {
        override val displayText = "Success"
    }
}

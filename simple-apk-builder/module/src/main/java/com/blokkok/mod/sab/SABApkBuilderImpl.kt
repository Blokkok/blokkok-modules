package com.blokkok.mod.sab

import androidx.fragment.app.Fragment
import com.blokkok.mod.aapem.extensions.AAPEMExtension
import java.io.File

class SABApkBuilderImpl : AAPEMExtension.ApkBuilder {
    override fun showApkBuilderFragment(
        cacheDir: File,
        manifestFile: File,
        javaSrc: File,
        resFolder: File,
        outputApk: File,
    ): Fragment =
        BuildFragment.newInstance(
            cacheDir,
            manifestFile,
            javaSrc,
            resFolder,
            outputApk
        )


    override val name: String get() = "Blokkok Simple Apk Builder"
}
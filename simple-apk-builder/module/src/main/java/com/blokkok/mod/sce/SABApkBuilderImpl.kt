package com.blokkok.mod.sce

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
    ): Fragment {
        TODO("Not yet implemented")
    }


    override val name: String get() = "Blokkok Simple Apk Builder"
}
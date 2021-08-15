package com.blokkok.mod.aapem

import androidx.fragment.app.Fragment
import java.io.File

sealed interface AAPEMExtension {
    val name: String

    interface CodeEditor : AAPEMExtension {
        fun showCodeEditorFragment(javaSrc: File): Fragment
    }

    interface LayoutEditor : AAPEMExtension {
        fun showLayoutEditorFragment(resLayout: File): Fragment
    }

    interface ApkBuilder : AAPEMExtension {
        fun showApkBuilderFragment(
            cacheDir: File,
            manifestFile: File,
            javaSrc: File,
            resFolder: File,
            outputApk: File,
        ): Fragment
    }
}
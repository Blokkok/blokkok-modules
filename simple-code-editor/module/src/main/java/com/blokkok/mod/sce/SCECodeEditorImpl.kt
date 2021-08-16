package com.blokkok.mod.sce

import androidx.fragment.app.Fragment
import com.blokkok.mod.aapem.extensions.AAPEMExtension
import java.io.File

class SCECodeEditorImpl : AAPEMExtension.CodeEditor {
    override fun showCodeEditorFragment(file: File): Fragment =
        CodeEditorFragment.newInstance(file)

    override val name: String get() = "Blokkok Simple Code Editor"
}
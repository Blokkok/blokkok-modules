package com.blokkok.mod.sle

import androidx.fragment.app.Fragment
import com.blokkok.mod.aapem.extensions.AAPEMExtension
import java.io.File

class SLELayoutEditorImpl : AAPEMExtension.LayoutEditor {
    override fun showLayoutEditorFragment(layoutFile: File): Fragment =
        LayoutEditorFragment.newInstance(layoutFile)

    override val name: String get() = "Blokkok Simple Layout Editor"
}
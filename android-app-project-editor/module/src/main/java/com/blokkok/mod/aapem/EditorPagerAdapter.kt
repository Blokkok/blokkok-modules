package com.blokkok.mod.aapem

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.io.File

class EditorPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val javaSrcFolder: File,
    private val resLayoutFolder: File,
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AAPEMImplementation.codeEditorImpl?.showCodeEditorFragment(javaSrcFolder)
                ?: NoImplFragment(
                    "AAPEM Code Editor Implementation cannot be found, " +
                    "Try loading a module named \"Simple Code Editor\""
                )

            1 -> AAPEMImplementation.layoutEditorImpl?.showLayoutEditorFragment(resLayoutFolder)
                ?: NoImplFragment(
                    "AAPEM Layout Editor Implementation cannot be found, " +
                    "Try loading a module named \"Simple Layout Editor\""
                )

//            2 -> AAPEMImplementation.manifestEditorImpl?.showManifestEditorFragment(javaSrcFolder)
//                ?: NoImplFragment("AAPEM Manifest Editor Implementation cannot be found")

            else -> throw IllegalArgumentException("Unknown fragment $position")
        }
    }
}
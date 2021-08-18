package com.blokkok.mod.sab

import com.blokkok.mod.aapem.AAPEMImplementation
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class SimpleApkBuilderModule : Module() {
    override val namespace: String get() = "blokkok-simple-code-editor"

    override fun onLoaded(comContext: CommunicationContext) {
        AssetsPaths.init(
            assets
                ?: throw IllegalStateException(
                    "Simple APK Builder Module's assets doesn't seem to have been extracted"
                )
        )
        AAPEMImplementation.registerImplementation(SABApkBuilderImpl())
    }

    override fun onUnloaded(comContext: CommunicationContext) {}
}
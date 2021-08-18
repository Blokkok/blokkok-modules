package com.blokkok.mod.sab

import com.blokkok.mod.aapem.AAPEMImplementation
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class SimpleApkBuilderModule : Module() {
    override val namespace: String get() = "blokkok-simple-code-editor"

    override fun onLoaded(comContext: CommunicationContext) {
        AAPEMImplementation.registerImplementation(SABApkBuilderImpl())
    }

    override fun onUnloaded(comContext: CommunicationContext) {}
}
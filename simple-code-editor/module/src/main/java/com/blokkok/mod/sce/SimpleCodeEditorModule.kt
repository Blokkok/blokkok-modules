package com.blokkok.mod.sce

import com.blokkok.mod.aapem.AAPEMImplementation
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class SimpleCodeEditorModule : Module() {
    override val namespace: String get() = "blokkok-simple-code-editor"

    override fun onLoaded(comContext: CommunicationContext) {
        AAPEMImplementation.registerImplementation(SCECodeEditorImpl())
    }

    override fun onUnloaded(comContext: CommunicationContext) {}
}
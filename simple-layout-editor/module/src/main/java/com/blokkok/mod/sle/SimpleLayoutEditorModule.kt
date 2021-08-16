package com.blokkok.mod.sle

import com.blokkok.mod.aapem.AAPEMImplementation
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class SimpleLayoutEditorModule : Module() {
    override val namespace: String get() = "blokkok-simple-layout-editor"

    override fun onLoaded(comContext: CommunicationContext) {
        AAPEMImplementation.registerImplementation(SLELayoutEditorImpl())
    }

    override fun onUnloaded(comContext: CommunicationContext) {}
}
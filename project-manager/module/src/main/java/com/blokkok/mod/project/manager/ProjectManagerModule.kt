package com.blokkok.mod.project.manager

import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class ProjectManagerModule : Module() {
    override val namespace: String get() = "blokkok-project-manager"

    override fun onLoaded(comContext: CommunicationContext) {
        // TODO: 8/5/21 Make this
    }

    override fun onUnloaded(comContext: CommunicationContext) {
        // TODO: 8/5/21 Make this
    }
}
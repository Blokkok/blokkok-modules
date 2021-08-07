package com.blokkok.mod.project.manager

import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class ProjectManagerModule : Module() {
    override val namespace: String get() = "blokkok-project-manager"

    override fun onLoaded(comContext: CommunicationContext) {
        comContext.run {
            val args = mapOf(
                "name" to "Projects",
                // TODO: 8/7/21 ICON
                "fragment" to ProjectsListFragment(),
            )

            invokeFunction("/essentials/main_drawer", "create_item", args)
        }
    }

    override fun onUnloaded(comContext: CommunicationContext) {
        // TODO: 8/5/21 Make this
    }
}
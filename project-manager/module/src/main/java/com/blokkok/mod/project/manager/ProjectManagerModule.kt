package com.blokkok.mod.project.manager

import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class ProjectManagerModule : Module() {
    override val namespace: String get() = "blokkok-project-manager"

    override fun onLoaded(comContext: CommunicationContext) {
        comContext.run {
            claimFlag(PROJECT_MANAGER_IMPL_FLAG)

            val args = mapOf(
                "name" to "Projects",
                "group" to "main",
                "order" to 20,
                // TODO: 8/7/21 ICON
                "fragment" to ProjectsListFragment(),
            )

            invokeFunction("/essentials/main_drawer", "create_item", args)
        }
    }

    // TODO: 8/10/21 Add onAllLoaded event and get flags

    override fun onUnloaded(comContext: CommunicationContext) { }
}
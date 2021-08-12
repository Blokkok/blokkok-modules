package com.blokkok.mod.project.manager

import android.content.Context
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class ProjectManagerModule : Module() {
    override val namespace: String get() = "blokkok-project-manager"

    override fun onLoaded(comContext: CommunicationContext) {
        comContext.run {
            claimFlag(PROJECT_MANAGER_IMPL_FLAG)

            val context = comContext.invokeFunction("get_application_context") as Context
            ProjectManager.initialize(context)

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

    override fun onAllLoaded(comContext: CommunicationContext) {
        // since onAllLoaded can be called everytime the user loaded all the modules even though
        // we have already been loaded, we needed to re-do the implementation checking again
        PMModuleImplsManager.clear()

        // get the flags
        PMModuleImplsManager.getImplementations(
            comContext.getFlagNamespaces(PROJECT_MANAGER_IMPL_FLAG),
            comContext
        )
    }

    override fun onUnloaded(comContext: CommunicationContext) { }
}
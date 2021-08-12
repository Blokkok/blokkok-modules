package com.blokkok.mod.aapem

import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class AAPEModule : Module() {
    override val namespace: String = "blokkok-aapem"
    override val flags: List<String> = listOf("project-manager-impl")

    override fun onLoaded(comContext: CommunicationContext) {
        comContext.run {

            // project manager implementation
            namespace("pm-impl") {
                createFunction("name") { "Blokkok AAPEM" }
                createFunction("new_project_config") {
                    listOf("App Name", "App Package")
                }

                createFunction("initialize_project") {
                    // TODO: 8/12/21 This
                }

                createFunction("show_project_editor") {
                    // TODO: 8/12/21 This
                }
            }
        }
    }

    override fun onUnloaded(comContext: CommunicationContext) {

    }
}
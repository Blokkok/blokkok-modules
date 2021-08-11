package com.blokkok.mod.project.manager

import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.communication.CommunicationType

object PMModuleImplsManager {
    //                                  mod_name  mod_namespace
    private val implementations = HashMap<String, String>()

    val implementationNames get() = implementations.keys

    fun getNamespace(name: String) = implementations[name]

    /**
     * Clears the implementation references
     */
    fun clear() {
        implementations.clear()
    }

    /**
     * Gets the implementation of the given namespaces
     */
    fun getImplementations(namespaces: List<String>, comContext: CommunicationContext) {
        for (namespace in namespaces) {
            if (implementedCorrectly(namespace, comContext)) {
                // get the name
                val name =
                    comContext.invokeFunction("/$namespace/pm-impl", "name") as String

                implementations[name] = namespace
            }
        }
    }

    /**
     * Needed communications for the project manager implementation
     */
    private val neededCommunications = mapOf(
        "name"                  to CommunicationType.FUNCTION,
        "new_project_config"    to CommunicationType.FUNCTION,
        "initialize_project"    to CommunicationType.FUNCTION,
        "show_project_editor"   to CommunicationType.FUNCTION,
    )

    /**
     * Used to check if the given namespace has implemented the required functions correctly
     */
    private fun implementedCorrectly(namespace: String, comContext: CommunicationContext): Boolean {
        for (communication in neededCommunications) {
            if (comContext
                    .getCommunication("/$namespace/pm-impl", communication.key)
                != communication.value
            ) return false
        }

        return true
    }
}
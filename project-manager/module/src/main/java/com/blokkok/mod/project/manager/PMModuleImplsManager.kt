package com.blokkok.mod.project.manager

import androidx.fragment.app.Fragment
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.communication.CommunicationType
import java.io.File

object PMModuleImplsManager {
    //                                  mod_name  mod_namespace
    private val implementations = HashMap<String, String>()

    // doing this feels very off, I'm quite used to not allowing Context on a static scope
    // also, yes, doing this is 100% A-OKAY, since the modules are also going to be stored
    // in a static context in the module system, the communication context is also going
    // to get garbage collected together with the modules
    private lateinit var comContext: CommunicationContext

    fun getNamespace(name: String) = implementations[name]

    /**
     * Clears the implementation references
     */
    fun clear() {
        implementations.clear()
    }

    /**
     * Opens the project editor
     */
    fun openProjectEditor(id: String, implementationName: String) {
        val implementationNamespace = PMModuleImplsManager.getNamespace(implementationName)

        // get the fragment
        val projectEditorFrag =
            comContext.invokeFunction(
                "/$implementationNamespace/pm-impl",
                "show_project_editor",
                mapOf("project_dir" to File(ProjectManager.projectsDir, id))
            ) as Fragment

        // show the fragment
        comContext.invokeFunction(
            "/essentials/fragment",
            "start_fragment",
            mapOf("fragment" to projectEditorFrag)
        )
    }

    /**
     * Gets the implementation of the given namespaces
     */
    fun getImplementations(namespaces: List<String>, comContext: CommunicationContext) {
        this.comContext = comContext
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
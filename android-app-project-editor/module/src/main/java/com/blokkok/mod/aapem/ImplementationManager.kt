package com.blokkok.mod.aapem

import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.exception.NotDefinedException

object ImplementationManager {

    //                                                name  namespace
    private val codeEditorImplementations = HashMap<String, String>()
    private val layoutEditorImplementations = HashMap<String, String>()
    private val apkBuilderImplementations = HashMap<String, String>()

    val codeEditors get() = codeEditorImplementations.keys
    val layoutEditors get() = layoutEditorImplementations.keys
    val apkBuilders get() = apkBuilderImplementations.keys

    private var codeEditorImplementation: String? = null
    private var layoutEditorImplementation: String? = null
    private var apkBuilderImplementation: String? = null

    private lateinit var comContext: CommunicationContext

    fun scanImplementations(comContext: CommunicationContext) {
        this.comContext = comContext

        // Reset everything
        codeEditorImplementations.clear()
        layoutEditorImplementations.clear()
        apkBuilderImplementations.clear()

        codeEditorImplementation = null
        layoutEditorImplementation = null
        apkBuilderImplementation = null

        for (namespace in comContext.getFlagNamespaces(CODE_EDITOR_IMPL_FLAG)) {
            if (checkCodeEditorImpl(namespace)) {
                val name = comContext
                    .invokeFunction(
                        "/$namespace/aapem-code-editor-impl",
                        "name"
                    ) as String

                codeEditorImplementations[name] = namespace
            }
        }

        for (namespace in comContext.getFlagNamespaces(LAYOUT_EDITOR_IMPL_FLAG)) {
            if (checkLayoutEditorImpl(namespace)) {
                val name = comContext
                    .invokeFunction(
                        "/$namespace/aapem-layout-editor-impl",
                        "name"
                    ) as String

                layoutEditorImplementations[name] = namespace
            }
        }

        for (namespace in comContext.getFlagNamespaces(APK_BUILDER_IMPL_FLAG)) {
            if (checkApkBuilderImpl(namespace)) {
                val name = comContext
                    .invokeFunction(
                        "/$namespace/aapem-apk-builder-impl",
                        "name"
                    ) as String

                apkBuilderImplementations[name] = namespace
            }
        }
    }

    fun chooseCodeEditorImplementation(name: String) {
        codeEditorImplementation = codeEditorImplementations[name]
    }

    fun chooseLayoutEditorImplementation(name: String) {
        layoutEditorImplementation = layoutEditorImplementations[name]
    }

    fun chooseApkBuilderImplementation(name: String) {
        apkBuilderImplementation = apkBuilderImplementations[name]
    }

    private fun checkCodeEditorImpl(namespace: String): Boolean {
        comContext.run {
            try {
                val implNamespace = "/$namespace/aapem-code-editor-impl"

                if (getCommunication(implNamespace, "name") == null) return false
                if (getCommunication(implNamespace, "open_code_editor") == null) return false

            } catch (e: NotDefinedException) {
                return false
            }
        }

        return true
    }

    private fun checkLayoutEditorImpl(namespace: String): Boolean {
        comContext.run {
            try {
                val implNamespace = "/$namespace/aapem-layout-editor-impl"

                if (getCommunication(implNamespace, "name") == null) return false
                if (getCommunication(implNamespace, "open_layout_editor") == null) return false

            } catch (e: NotDefinedException) {
                return false
            }
        }

        return true
    }

    private fun checkApkBuilderImpl(namespace: String): Boolean {
        comContext.run {
            try {
                val implNamespace = "/$namespace/aapem-apk-builder-impl"

                if (getCommunication(implNamespace, "name") == null) return false
                if (getCommunication(implNamespace, "build_apk") == null) return false

            } catch (e: NotDefinedException) {
                return false
            }
        }

        return true
    }
}
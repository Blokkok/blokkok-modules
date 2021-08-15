package com.blokkok.mod.aapem

object AAPEMImplementation {

    val codeEditors = HashMap<String, AAPEMExtension.CodeEditor>()
    val layoutEditors = HashMap<String, AAPEMExtension.LayoutEditor>()
    val apkBuilders = HashMap<String, AAPEMExtension.ApkBuilder>()

    var codeEditorImpl: AAPEMExtension.CodeEditor? = null
    var layoutEditorImpl: AAPEMExtension.LayoutEditor? = null
    var apkBuilderImpl: AAPEMExtension.ApkBuilder? = null

    fun registerImplementation(extension: AAPEMExtension) {
        when (extension) {
            is AAPEMExtension.CodeEditor -> {
                if (extension.name in codeEditors) return

                codeEditors[extension.name] = extension
            }

            is AAPEMExtension.LayoutEditor -> {
                if (extension.name in layoutEditors) return

                layoutEditors[extension.name] = extension
            }

            is AAPEMExtension.ApkBuilder -> {
                if (extension.name in apkBuilders) return

                apkBuilders[extension.name] = extension
            }
        }
    }
}
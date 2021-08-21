package com.blokkok.mod.sab

import android.content.Context
import com.blokkok.mod.aapem.AAPEMImplementation
import com.blokkok.mod.sab.managers.CommonFilesManager
import com.blokkok.mod.sab.managers.NativeBinariesManager
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class SimpleApkBuilderModule : Module() {
    override val namespace: String get() = "blokkok-simple-apk-builder"

    override fun onLoaded(comContext: CommunicationContext) {
        AssetsPaths.init(
            assets
                ?: throw IllegalStateException(
                    "Simple APK Builder Module's assets doesn't seem to have been extracted"
                )
        )
        val context = comContext.invokeFunction("application_context") as Context

        CommonFilesManager.initialize(context, assets!!)
        NativeBinariesManager.initialize(context, assets!!)

        AAPEMImplementation.registerImplementation(SABApkBuilderImpl())
    }

    override fun onUnloaded(comContext: CommunicationContext) {}
}
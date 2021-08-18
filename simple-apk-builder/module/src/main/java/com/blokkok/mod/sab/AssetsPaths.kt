package com.blokkok.mod.sab

import java.io.File

object AssetsPaths {
    val assetsFolder get() = assetsFolder_
    private lateinit var assetsFolder_: File

    fun init(assetsFolder: File) {
        this.assetsFolder_ = assetsFolder
    }

    val androidJar get() = assetsFolder.resolve("android.jar")
    val apksignerJar get() = assetsFolder.resolve("apksigner/apksigner.jar")
    val binariesZip get() = assetsFolder.resolve("binaries.zip")
    val d8Jar get() = assetsFolder.resolve("d8/d8.jar")
    val ecjJar get() = assetsFolder.resolve("ecj/ecj.jar")
    val testkeyZip get() = assetsFolder.resolve("testkey.zip")
}
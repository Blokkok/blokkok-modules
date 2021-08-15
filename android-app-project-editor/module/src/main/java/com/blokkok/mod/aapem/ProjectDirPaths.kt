package com.blokkok.mod.aapem

import java.io.File

/**
 * Very simple class used so the code looks clean, more readable, and centralized in here. If we
 * need to change the file structure, we can do it here instead of changing each one that references
 * the thing that we wanted to change
 */
class ProjectDirPaths(
    val projectDir: File
) {
    val cacheDir: File get() = projectDir.resolve("cache")
    val outputApk: File get() = projectDir.resolve("out-debug.apk")

    val srcFolder: File get() = projectDir.resolve("src")
    val javaSrc: File get() = srcFolder.resolve("java")

    val resFolder: File get() = srcFolder.resolve("res")
    val resLayout: File get() = resFolder.resolve("layout")

    val manifestFile: File get() = projectDir.resolve("AndroidManifest.xml")
}
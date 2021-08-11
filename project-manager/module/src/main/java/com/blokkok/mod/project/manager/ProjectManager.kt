package com.blokkok.mod.project.manager

import android.content.Context
import com.blokkok.modsys.communication.CommunicationContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object ProjectManager {
    lateinit var projectsDir: File

    fun initialize(context: Context, comContext: CommunicationContext) {
        projectsDir = File(context.applicationInfo.dataDir, "projects")

        // create the projects folder if it doesn't exist
        if (!projectsDir.exists()) projectsDir.mkdirs()
    }

    fun listProjects(): List<ProjectMetadata> =
        projectsDir.listFiles()!!.map {
            Json.decodeFromString(it.resolve("project-metadata.json").readText())
        }

    fun exists(id: String): Boolean = listProjects().any { it.id == id }

    fun getProject(id: String): ProjectMetadata? {
        if (!exists(id)) return null

        return Json.decodeFromString(File(projectsDir, "$id/meta.json").readText())
    }

    fun removeProject(id: String): Boolean {
        if (!exists(id)) return false
        return File(projectsDir, id).deleteRecursively()
    }

    fun modifyMetadata(id: String, newMeta: ProjectMetadata) {
        if (!exists(id)) return

        File(projectsDir, id)
            .resolve("meta.json")
            .writeText(Json.encodeToString(newMeta))
    }

    fun createProject(name: String, conf: Map<String, String>, implName: String): ProjectMetadata {
        val id = generateRandomId()
        val metadata = ProjectMetadata(id, name, conf, implName)
        val projectDir = File(projectsDir, id)

        projectDir.mkdir()

        File(projectDir, "android").mkdir()     // Files required by the compiler
        File(projectDir, "data").mkdir()        // Project files
        File(projectDir, "cache").mkdir()       // Cache folder

        File(projectDir, "meta.json").writeText(Json.encodeToString(metadata))

        return metadata
    }

    private fun generateRandomId(): String {
        val projects = listProjects()
        var id: String

        do {
            id = List(16) {
                (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
            }.joinToString("")

        } while (projects.any { it.id == id }) // This checks if the generated id already exists in the projects list

        return id
    }
}
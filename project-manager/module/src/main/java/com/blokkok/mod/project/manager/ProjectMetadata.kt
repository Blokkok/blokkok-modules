package com.blokkok.mod.project.manager

import kotlinx.serialization.Serializable

@Serializable
data class ProjectMetadata(
    val id: String,
    val name: String,
    val configuration: Map<String, String>,
    val implementationName: String,
)
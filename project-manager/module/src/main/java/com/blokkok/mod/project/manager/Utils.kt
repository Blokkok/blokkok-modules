package com.blokkok.mod.project.manager

import android.content.res.Resources

// Used to very easily convert numbers into dp in a beautiful way
// Thanks from: https://gist.github.com/titoaesj/ccd7ddc3c40350217f2bcae248d2ffc3

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

const val PROJECT_MANAGER_IMPL_FLAG = "project-manager-impl"

/*
 * For modules that implemented this flag, they will need to have these communications implemented:
 * namespace "pm-impl" {
 *     func "name" -> String
 *     | Returns the name of this implementation, used to distinguish with other modules when there
 *     | are multiple implementations
 *
 *     func "new_project_config" -> List<String>
 *     | Returns the configuration names needed when the user wants to create a project, example: package name
 *     | Currently, this module can only have String as values
 *
 *     func "initialize_project" args [ "project_dir": File, "project_config": Map<String, String> ] -> Unit
 *     | Will be called when the user creates a project, you can initialize some stuff here
 *
 *     func "show_project_editor" args [ "project_dir": File ] -> Fragment
 *     | Used to show the project editor fragment when the user clicked on a project
 *     | If the user made a project, this will be called after func "initialize_project"
 * }
 */
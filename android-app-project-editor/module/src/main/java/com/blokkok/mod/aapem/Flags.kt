package com.blokkok.mod.aapem

// flags where other modules can use to implement features needed for this module

const val CODE_EDITOR_IMPL_FLAG = "aapem-code-editor-impl"
/* Modules with this flag must implement these communication:
 *
 * namespace "aapem-code-editor-impl" {
 *     func "name" -> String
 *     | Thi function will be used as a display name to distinguish this implementation with other
 *     | implementations
 *
 *     func "open_code_editor" [ args "file": java.io.File ] -> androidx.fragment.app.Fragment
 *     | This function will need to instantiate the code editor fragment where it will be
 *     | displayed inside a ViewPager2
 * }
 *
 */

const val LAYOUT_EDITOR_IMPL_FLAG = "aapem-layout-editor-impl"
/* Modules with this flag must implement these communications:
 *
 * namespace "aapem-layout-editor-impl" {
 *     func "name" -> String
 *     | Thi function will be used as a display name to distinguish this implementation with other
 *     | implementations
 *
 *     func "open_layout_editor" [ args "file": java.io.File ] -> androidx.fragment.app.Fragment
 *     | This function will need to instantiate the layout editor fragment where it will be
 *     | displayed inside a ViewPager2
 * }
 *
 */

const val APK_BUILDER_IMPL_FLAG = "aapem-apk-builder-impl"
/* Modules with this flag must implement these communications:
 *
 * namespace "aapem-apk-builder-impl" {
 *     func "name" -> String
 *     | Thi function will be used as a display name to distinguish this implementation with other
 *     | implementations
 *
 *     func "build_apk" [ args
 *                          "cache_dir": java.io.File,
 *                          "android_manifest": java.io.File,
 *                          "java_src": java.io.File,
 *                          "res_folder": java.io.File,
 *                          "output_apk": java.io.File,
 *                          "log": (String) -> Unit,
 *                          "finish_callback": () -> Unit,
 *                          "error_callback": () -> Unit,
 *                      ]
 *
 *     | This function will need to build an apk using the source code specified (manifest, java,
 *     | res) into the specified "output_apk" argument (this will point to a non-existent file).
 *     | Make sure to put your compiled files in the cache dir specified.
 * }
 *
 */
package com.blokkok.mod.essentials

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module

class Essentials : Module() {
    override val namespace: String get() = "essentials"
    override val flags: List<String> get() = listOf(
        "Main_onNavigationItemSelectedListener" // for listening to onNavItemSelected
    )

    // A list of pair of menu item name and it's fragment
    private val registeredDrawerItems: ArrayList<Pair<String, Fragment>> = ArrayList()

    @Suppress("UNCHECKED_CAST")
    override fun onLoaded(comContext: CommunicationContext) {
        comContext.run {
            val context = comContext.invokeFunction("get_application_context") as Context
            val fragmentManager = comContext.invokeFunction("support_fragment_manager") as FragmentManager
            val drawerFragContainerId = comContext.invokeFunction("drawer_fragment_container_id") as Int
            val mainFragContainerId = comContext.invokeFunction("main_fragment_container_id") as Int
            val mainDrawerMenu = comContext.invokeFunction("main_drawer_menu") as Menu

            // Essential utilities =====

            createFunction("toast") { args ->
                val text = args["text"] as String
                val duration = args["duration"] as? Int

                Toast.makeText(context, text, duration ?: Toast.LENGTH_SHORT).show()

                return@createFunction null
            }

            createFunction("alert_dialog") { args ->
                val title = args["title"] as String
                val content = args["content"] as String
                val positiveText = args["positive_text"] as? String
                val positiveHandler = args["positive_handler"] as? ((DialogInterface) -> Unit)
                val negativeText = args["negative_text"] as? String
                val negativeHandler = args["negative_handler"] as? ((DialogInterface) -> Unit)

                val builder = AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(content)

                positiveText?.let {
                    builder.setPositiveButton(it) { dialog, _ ->
                        positiveHandler?.invoke(dialog)
                    }
                }

                negativeText?.let {
                    builder.setNegativeButton(it) { dialog, _ ->
                        negativeHandler?.invoke(dialog)
                    }
                }

                builder.create().show()

                return@createFunction null
            }

            // Utilities in doing fragment-related stuff
            namespace("fragment") {
                createFunction("start_fragment") { args ->
                    val fragment = args["fragment"] as? Fragment
                    val view = args["view"] as? View

                    val transaction = fragmentManager.beginTransaction()

                    transaction
                        .replace(mainFragContainerId,
                            when {
                                fragment != null -> fragment
                                view != null -> ModifiableViewFragment { _,_,_ -> view }

                                else -> throw IllegalArgumentException(
                                    "There must be either one non-null argument from fragment or view"
                                )
                            }
                        )
                        .addToBackStack(null)
                        .commit()

                    return@createFunction null
                }

                createFunction("go_back") {
                    fragmentManager.popBackStack()
                }
            }

            // Utilities in modifying the drawer on the main page
            namespace("main_drawer") {
                createFunction("create_item") { args ->
                    val name = args["name"] as String
                    val icon = args["icon"] as? Bitmap
                    val view = args["view"] as? View
                    val fragment = args["fragment"] as? Fragment

                    mainDrawerMenu.add(name)
                    val menuItem = mainDrawerMenu.children.find { it.title == name }!!

                    icon?.let { menuItem.icon = BitmapDrawable(context.resources, icon) }

                    when {
                        view != null -> {
                            // then use the ModifiableViewFragment
                            registeredDrawerItems
                                .add(
                                    Pair(
                                        name,
                                        ModifiableViewFragment { _, _, _ -> view }
                                    )
                                )
                        }

                        fragment != null -> {
                            // this module decided to make their own module, OK!
                            registeredDrawerItems
                                .add(
                                    Pair(
                                        name,
                                        fragment
                                    )
                                )
                        }

                        else ->
                            // you need to have at least the view or fragment
                            throw IllegalArgumentException("You need to at least provide a view or a fragment")
                    }

                    return@createFunction null
                }
            }

            // For listening to MainActivity's onNavigationItemSelected, don't use this on your module
            createFunction("onNavigationItemSelected") {
                val menuItem = it["menu_item"] as MenuItem

                for (item in registeredDrawerItems) {
                    val name = item.first
                    val fragment = item.second

                    if (menuItem.title == name) {
                        fragmentManager
                            .beginTransaction()
                            .replace(drawerFragContainerId, fragment)
                            .addToBackStack(null)
                            .commit()

                        return@createFunction true
                    }
                }

                return@createFunction false
            }
        }
    }

    override fun onUnloaded(comContext: CommunicationContext) {

    }
}
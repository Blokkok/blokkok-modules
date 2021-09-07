package com.blokkok.mod.essentials

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module
import com.blokkok.modsys.modinter.annotations.Function
import com.blokkok.modsys.modinter.annotations.Namespace
import kotlin.properties.Delegates

class Essentials : Module() {
    override val namespace: String get() = "essentials"
    override val flags: List<String> get() = listOf(
        "Main_onNavigationItemSelectedListener" // for listening to onNavItemSelected
    )

    private lateinit var context: Context
    private lateinit var fragmentManager: FragmentManager
    private lateinit var mainDrawerMenu: Menu

    private var drawerFragContainerId by Delegates.notNull<Int>()
    private var mainFragContainerId by Delegates.notNull<Int>()
    private var drawerMainGroupId by Delegates.notNull<Int>()
    private var drawerSocialGroupId by Delegates.notNull<Int>()

    // A list of pair of menu item name and it's fragment
    private val registeredDrawerItems: ArrayList<DrawerItem> = ArrayList()

    @Suppress("UNCHECKED_CAST")
    override fun onLoaded(comContext: CommunicationContext) {
        comContext.run {
            context = invokeFunction("get_application_context") as Context
            fragmentManager = invokeFunction("support_fragment_manager") as FragmentManager
            drawerFragContainerId = invokeFunction("drawer_fragment_container_id") as Int
            mainFragContainerId = invokeFunction("main_fragment_container_id") as Int
            mainDrawerMenu = invokeFunction("main_drawer_menu") as Menu

            drawerMainGroupId = invokeFunction("drawer_menu_main_group_id") as Int
            drawerSocialGroupId = invokeFunction("drawer_menu_social_group_id") as Int

            // For listening to MainActivity's onNavigationItemSelected, don't use this on your module
            createFunction("onNavigationItemSelected") {
                val menuItem = it["menu_item"] as MenuItem

                for (item in registeredDrawerItems) {
                    val name = item.name

                    if (menuItem.title == name) {
                        fragmentManager
                            .beginTransaction()
                            .replace(drawerFragContainerId, item.createFragment())
                            .addToBackStack(null)
                            .commit()

                        return@createFunction true
                    }
                }

                return@createFunction false
            }
        }
    }

    override fun onUnloaded(comContext: CommunicationContext) {}

    @Function("toast")
    fun doToast(text: String, duration: Int? = null) {
        Toast.makeText(context, text, duration ?: Toast.LENGTH_SHORT).show()
    }

    @Function("alert_dialog")
    fun alertDialog(
        title: String,
        content: String,
        positiveText: String? = null,
        positiveHandler: ((DialogInterface) -> Unit)? = null,
        negativeText: String? = null,
        negativeHandler: ((DialogInterface) -> Unit)? = null,
    ) {
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
    }

    // Utilities in doing fragment-related stuff
    // TODO: 9/7/21 make Namespaces possible with inner classes
    @Namespace(name = "fragment")
    inner class FragmentNM {
        @Function("start_fragment")
        fun startFragment(
            fragment: Fragment? = null,
            view: View? = null,
        ) {
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
        }

        @Function("go_back")
        fun goBack() {
            fragmentManager.popBackStack()
        }
    }

    // Utilities in modifying the drawer on the main page
    @Namespace("main_drawer")
    inner class MainDrawer {
        @Function("create_item")
        fun createItem(
            name: String,
            _group: String? = null,
            orderInCategory: Int? = null,
            icon: Bitmap? = null,
            view: View? = null,
            fragment: Fragment? = null,
        ): MenuItem {
            val group = _group?.let {
                when (it) {
                    "main" -> drawerMainGroupId
                    "social" -> drawerSocialGroupId
                    else -> null
                }
            }

            val menuItem = mainDrawerMenu.add(
                group ?: Menu.NONE,
                Menu.NONE,
                orderInCategory ?: Menu.NONE,
                name
            )

            icon?.let { menuItem.icon = BitmapDrawable(context.resources, icon) }

            when {
                view != null -> {
                    // then use the ModifiableViewFragment
                    registeredDrawerItems
                        .add(
                            DrawerItem(name) { ModifiableViewFragment { _, _, _ -> view } }
                        )
                }

                fragment != null -> {
                    // this module decided to make their own module, OK!
                    registeredDrawerItems
                        .add(
                            DrawerItem(name) { fragment }
                        )
                }

                else ->
                    // you need to have at least the view or fragment
                    throw IllegalArgumentException("You need to at least provide a view or a fragment")
            }

            return menuItem
        }
    }
}

data class DrawerItem(
    val name: String,
    val createFragment: () -> Fragment,
)
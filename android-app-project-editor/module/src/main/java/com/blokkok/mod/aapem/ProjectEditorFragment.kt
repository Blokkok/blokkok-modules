package com.blokkok.mod.aapem

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File

class ProjectEditorFragment(
    val projectDir: ProjectDirPaths
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val context = requireContext()

        return ConstraintLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            val appbarLayoutId: Int

            addView(AppBarLayout(context).apply {
                tag = "appbar"
                appbarLayoutId = id

                layoutParams = ConstraintLayout.LayoutParams(
                    0,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ).apply {
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                }

                addView(Toolbar(context).apply {
                    tag = "toolbar"

                    layoutParams = AppBarLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT,
                    ).apply {
                        scrollFlags =
                            AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL

                        title = "Editor"
                    }

                    val chooseImplItem = menu.add("Choose Implementation(s)")
                    chooseImplItem.setOnMenuItemClickListener {
                        val args = mapOf(
                            "fragment" to ImplementationChooserFragment()
                        )

                        AAPEModule
                            .comContext
                            .invokeFunction("/essentials", "show_fragment", args)

                        true
                    }
                })

                addView(TabLayout(context).apply {
                    tag = "tab_layout"

                    layoutParams = AppBarLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT,
                    )

                    tabGravity = TabLayout.GRAVITY_CENTER
                    tabMode = TabLayout.MODE_FIXED
                })
            })

            addView(LinearLayout(context).apply {
                layoutParams = ConstraintLayout
                    .LayoutParams(0, 0)
                    .apply {
                        bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                        endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                        startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        topToBottom = appbarLayoutId
                    }

                addView(ViewPager2(context).apply {
                    tag = "editor_view_pager"

                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                    ).apply {
                        weight = 1f
                    }
                })

                addView(FrameLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                    ).apply {
                        weight = 0f
                    }

                    gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    elevation = 8f
                    setBackgroundColor(0xffffffff.toInt())

                    addView(MaterialButton(context).apply {
                        tag = "compile_button"

                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                        )

                        text = "Compile"
                    })
                })
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewWithTag<ViewPager2>("fragments_view_pager")
        val tabLayout = view.findViewWithTag<TabLayout>("tab_layout")
        val compileButton = view.findViewWithTag<Button>("compile_button")

        val editorAdapter = EditorPagerAdapter(
            requireActivity(),
            projectDir.javaSrc,
            projectDir.resLayout,
        )

        viewPager.adapter = editorAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "LAYOUT"
                1 -> tab.text = "CODE"
//                2 -> tab.text = "MANIFEST"
            }
        }.attach()

        compileButton.setOnClickListener {
            // check if there is any impl
            AAPEMImplementation.apkBuilderImpl?.let {
                // alright, instantiate the fragment
                val args = mapOf(
                    "fragment" to it.showApkBuilderFragment(
                        projectDir.cacheDir,
                        projectDir.manifestFile,
                        projectDir.javaSrc,
                        projectDir.resFolder,
                        projectDir.outputApk
                    )
                )

                // then launch it!
                AAPEModule
                    .comContext
                    .invokeFunction("/essentials", "show_fragment", args)

                true // so that we wouldn't trigger the toast

            } ?: Toast.makeText(
                requireContext(),
                "There is no implementation of AAPEM Apk Builder, Try loading a module named \"Simple Apk Builder\"",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
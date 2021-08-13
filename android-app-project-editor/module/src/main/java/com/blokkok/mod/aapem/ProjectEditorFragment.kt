package com.blokkok.mod.aapem

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout

class ProjectEditorFragment : Fragment() {

    private val toolbarMenu by lazy {

    }

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

                        // FIXME: 8/13/21 Menu can't be built programmatically :(

                        title = "Editor"
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

        // TODO: 8/13/21 setup viewpager2 and compile button
    }
}
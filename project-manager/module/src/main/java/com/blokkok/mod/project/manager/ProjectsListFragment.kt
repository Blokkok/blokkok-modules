package com.blokkok.mod.project.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProjectsListFragment : Fragment() {

    private lateinit var addProjectFAB: FloatingActionButton
    private lateinit var projectsListRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Build the view on runtime :(
        // yes this is very tedious and not pretty, but soon when we might find a way to inflate
        // compiled XML binaries, this process will go away and we would get a similar way to
        // find and get our views like android! I have high hopes! :^)
        val context = requireContext()
        val root = FrameLayout(context)

        projectsListRecyclerView =
            RecyclerView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    /* width */ FrameLayout.LayoutParams.MATCH_PARENT,
                    /* height */ FrameLayout.LayoutParams.MATCH_PARENT
                )
            }

        root.addView(projectsListRecyclerView)

        addProjectFAB =
            FloatingActionButton(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    /* width */ FrameLayout.LayoutParams.WRAP_CONTENT,
                    /* height */ FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    updateMargins(bottom = 16.dp, right = 16.dp)
                }
            }

        root.addView(addProjectFAB)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 8/7/21 Implement listing projects
    }
}
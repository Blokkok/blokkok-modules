package com.blokkok.mod.project.manager

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ProjectsListFragment : Fragment() {

    private lateinit var addProjectFAB: ExtendedFloatingActionButton
    private lateinit var projectsListRecyclerView: RecyclerView
    private lateinit var projectsAdapter: ProjectRecyclerViewAdapter

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
        val root = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }

        projectsListRecyclerView =
            RecyclerView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    /* width */ FrameLayout.LayoutParams.MATCH_PARENT,
                    /* height */ FrameLayout.LayoutParams.MATCH_PARENT
                )
            }

        root.addView(projectsListRecyclerView)

        addProjectFAB =
            ExtendedFloatingActionButton(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    /* width */ FrameLayout.LayoutParams.WRAP_CONTENT,
                    /* height */ FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    updateMargins(bottom = 16.dp, right = 16.dp)
                    gravity = Gravity.END or Gravity.BOTTOM

                    text = "Create Project"
                }
            }

        root.addView(addProjectFAB)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectsAdapter = ProjectRecyclerViewAdapter()
        projectsAdapter.projects.clear()
        projectsAdapter.projects.addAll(ProjectManager.listProjects())

        projectsListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        projectsListRecyclerView.adapter = projectsAdapter

        addProjectFAB.setOnClickListener {

        }
    }
}
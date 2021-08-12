package com.blokkok.mod.project.manager

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ProjectsListFragment : Fragment() {

    private lateinit var addProjectFAB: ExtendedFloatingActionButton
    private lateinit var projectsListRecyclerView: RecyclerView
    private lateinit var implementationChooserSpinner: Spinner
    private lateinit var projectsAdapter: ProjectRecyclerViewAdapter

    private val implementationAdapter by lazy {
        ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item
        )
    }

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

        val linear = LinearLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            orientation = LinearLayout.VERTICAL
        }

        root.addView(linear)

        implementationChooserSpinner = Spinner(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                /* width */ LinearLayout.LayoutParams.MATCH_PARENT,
                /* height */ LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                weight = 0f
            }
        }

        linear.addView(implementationChooserSpinner)

        projectsListRecyclerView =
            RecyclerView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    /* width */ LinearLayout.LayoutParams.MATCH_PARENT,
                    /* height */ LinearLayout.LayoutParams.WRAP_CONTENT,
                ).apply {
                    weight = 1f
                }
            }

        linear.addView(projectsListRecyclerView)

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

        val implementationNames = PMModuleImplsManager.implementationNames.toList()

        implementationChooserSpinner.adapter = implementationAdapter
        implementationAdapter.addAll(implementationNames)

        implementationChooserSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    PMModuleImplsManager.currentImplementation = implementationNames[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    PMModuleImplsManager.currentImplementation = null
                }
            }

        projectsListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        projectsListRecyclerView.adapter = projectsAdapter

        addProjectFAB.setOnClickListener {
            val context = requireContext()

            // check if the user has chosen an implementation
            if (PMModuleImplsManager.currentImplementation == null) {
                Toast.makeText(context,
                    "You need to select an implementation",
                    Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            val builder = AlertDialog.Builder(context)
            val layout = LinearLayout(context)

            layout.orientation = LinearLayout.VERTICAL

            val projectName = EditText(context)
            projectName.hint = "Project Name"
            layout.addView(projectName)

            // apply the new project configuration
            val newProjConf = PMModuleImplsManager.getProjectConfiguration()

            val editTexts = newProjConf.associateWith {
                val editText = EditText(context)
                editText.hint = it
                layout.addView(editText)

                editText
            }

            builder
                .setView(layout)
                .setTitle("New Project")
                .setPositiveButton("Create") { _, _ ->
                    // get all of the text of the editexts and put them into a map
                    val conf = editTexts.entries.associate { Pair(it.key, it.value.text.toString()) }

                    // then create the project
                    ProjectManager.createProject(
                        projectName.text.toString(),
                        conf,
                        PMModuleImplsManager.currentImplementation!!
                    )
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }
}
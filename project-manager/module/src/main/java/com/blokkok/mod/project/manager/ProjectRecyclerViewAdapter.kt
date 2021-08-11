package com.blokkok.mod.project.manager

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ProjectRecyclerViewAdapter() : RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder>() {
    val projects = ArrayList<ProjectMetadata>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            FrameLayout(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )

                addView(CardView(parent.context).apply {
                    tag = "card"

                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                    ).apply {
                        setMargins(16.dp, 8.dp, 16.dp, 8.dp)
                        setPadding(16.dp, 16.dp, 16.dp, 16.dp)
                    }

                    cardElevation = 5.dp.toFloat()
                    radius = 8.dp.toFloat()

                    addView(LinearLayout(parent.context).apply {
                        addView(TextView(parent.context).apply {
                            tag = "project_name"

                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                            setTextColor(Color.parseColor("#000"))
                        })

                        addView(TextView(parent.context).apply {
                            tag = "implementation"

                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                            setTextColor(Color.parseColor("#808080"))
                        })
                    })
                })
            }
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = projects[position]

        holder.projectName.text = project.name
        holder.implementation.text = project.implementationName

        holder.card.setOnClickListener {
            PMModuleImplsManager.openProjectEditor(project.id, project.implementationName)
        }
    }

    override fun getItemCount(): Int = projects.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: View = itemView.findViewWithTag("card")
        val projectName: TextView = itemView.findViewWithTag("project_name")
        val implementation: TextView = itemView.findViewWithTag("implementation")
    }
}
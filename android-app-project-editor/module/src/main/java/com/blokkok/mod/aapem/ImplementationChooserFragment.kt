package com.blokkok.mod.aapem

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setPadding
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment

class ImplementationChooserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val context = requireContext()

        return ScrollView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            addView(LinearLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )

                setPadding(8.dp)
                orientation = LinearLayout.VERTICAL

                // =================================================

                addView(TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )

                    setTextColor(0xFFBBBBBB.toInt())
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    text = "Code Editor Implementation"
                })

                addView(Spinner(context).apply {
                    tag = "code_editor_spinner"
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    ).apply {
                        updateMargins(bottom = 16.dp)
                    }
                })

                // =================================================

                addView(TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )

                    setTextColor(0xFFB0B0B0.toInt())
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    text = "Layout Editor Implementation"
                })

                addView(Spinner(context).apply {
                    tag = "layout_editor_spinner"
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    ).apply {
                        updateMargins(bottom = 16.dp)
                    }
                })

                // =================================================

                addView(TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )

                    setTextColor(0xFFB0B0B0.toInt())
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    text = "Apk Builder Implementation"
                })

                addView(Spinner(context).apply {
                    tag = "apk_builder_spinner"
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    ).apply {
                        updateMargins(bottom = 16.dp)
                    }
                })
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val codeEditorSpinner = view.findViewWithTag<Spinner>("code_editor_spinner")
        val layoutEditorSpinner = view.findViewWithTag<Spinner>("layout_editor_spinner")
        val apkBuilderSpinner = view.findViewWithTag<Spinner>("apk_builder_spinner")

        val supportSimpleDropdownItem = requireContext().resources.getIdentifier(
            "support_simple_spinner_dropdown_item",
            "layout",
            null
        )

        with(codeEditorSpinner) {
            adapter = ArrayAdapter(
                requireContext(),
                supportSimpleDropdownItem,
                AAPEMImplementation.codeEditors.keys.toList()
            )

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    AAPEMImplementation.codeEditorImpl =
                        AAPEMImplementation.codeEditors[parent!!.selectedItem as String]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    AAPEMImplementation.codeEditorImpl = null
                }
            }
        }

        with(layoutEditorSpinner) {
            adapter = ArrayAdapter(
                requireContext(),
                supportSimpleDropdownItem,
                AAPEMImplementation.layoutEditors.keys.toList()
            )

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    AAPEMImplementation.layoutEditorImpl =
                        AAPEMImplementation.layoutEditors[parent!!.selectedItem as String]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    AAPEMImplementation.layoutEditorImpl = null
                }
            }
        }

        with(apkBuilderSpinner) {
            adapter = ArrayAdapter(
                requireContext(),
                supportSimpleDropdownItem,
                AAPEMImplementation.apkBuilders.keys.toList()
            )

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    AAPEMImplementation.apkBuilderImpl =
                        AAPEMImplementation.apkBuilders[parent!!.selectedItem as String]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    AAPEMImplementation.apkBuilderImpl = null
                }
            }
        }
    }
}
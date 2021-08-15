package com.blokkok.mod.aapem

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class NoImplFragment(
    val implementationName: String
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val context = requireContext()
        return LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL

            addView(TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )

                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 26f, context.resources.displayMetrics)

                setTextColor(
                    context.resources.getColor(
                        context.resources.getIdentifier(
                            "material_on_background_emphasis_high_type",
                            null,
                            null
                        )
                    )
                )

                text = "No Implementation Found"
            })

            addView(TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )

                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, context.resources.displayMetrics)

                setTextColor(
                    context.resources.getColor(
                        context.resources.getIdentifier(
                            "material_on_background_emphasis_medium_type",
                            null,
                            null
                        )
                    )
                )

                text = implementationName
            })
        }
    }
}
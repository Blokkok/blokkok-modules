package com.blokkok.mod.sce

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.io.File
import java.util.concurrent.Executors

class CodeEditorFragment : Fragment() {

    private lateinit var file: File

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        args?.let {
            file = File(it.getString("path")!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val context = requireContext()

        return FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            addView(ExtendedFloatingActionButton(context).apply {
                tag = "save"

                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                ).apply {
                    gravity = Gravity.END or Gravity.BOTTOM
                }

                updatePadding(right = 16.dp, bottom = 16.dp)
                text = "Save"
            })

            addView(EditText(context).apply {
                tag = "edittext_code"

                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                )

                setBackgroundColor(0x00000000)
                fontFeatureSettings = "monospace"

                hint = "Code for ${file.nameWithoutExtension}"
            })
        }
    }

    private val saveExecutor = Executors.newCachedThreadPool()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val codeEditText = view.findViewWithTag<EditText>("edittext_code")
        val saveButton = view.findViewWithTag<ExtendedFloatingActionButton>("save")

        saveButton.setOnClickListener {
            val finishCallback = Runnable {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(it.context, "File saved", Toast.LENGTH_SHORT).show()
                }
            }

            saveExecutor.submit {
                val code = codeEditText.text.toString()
                file.writeText(code)

                finishCallback.run()
            }
        }
    }

    companion object {
        fun newInstance(file: File): CodeEditorFragment =
            CodeEditorFragment().apply {
                arguments = Bundle().apply {
                    putString("path", file.absolutePath)
                }
            }
    }
}
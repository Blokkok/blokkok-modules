package com.blokkok.mod.sab

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import java.io.File
import java.util.concurrent.Executors

class BuildFragment : Fragment() {

    private lateinit var cacheDir: File
    private lateinit var manifestFile: File
    private lateinit var javaSrc: File
    private lateinit var resFolder: File
    private lateinit var outputApk: File

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        args?.let {
            cacheDir = File(args.getString("cacheDir")!!)
            manifestFile = File(args.getString("manifestFile")!!)
            javaSrc = File(args.getString("javaSrc")!!)
            resFolder = File(args.getString("resFolder")!!)
            outputApk = File(args.getString("outputApk")!!)
        }
    }

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

                orientation = LinearLayout.VERTICAL
                setPadding(8.dp)

                // TODO: 8/18/21 Use recyclerview because this can be very long and can cause
                //       performance issues
                addView(TextView(context).apply {
                    tag = "build_log"

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )

                    fontFeatureSettings = "monospace"
                    val materialTextOnBackgroundId = resources
                            .getIdentifier(
                                "material_on_background_emphasis_high_type",
                                "color",
                                null
                            )

                    setTextColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        resources.getColor(materialTextOnBackgroundId, context.theme)
                    } else {
                        resources.getColor(materialTextOnBackgroundId)
                    })
                })
            })
        }
    }

    private val compileExecutor = Executors.newSingleThreadExecutor()

    companion object {
        fun newInstance(
            cacheDir: File,
            manifestFile: File,
            javaSrc: File,
            resFolder: File,
            outputApk: File,
        ): BuildFragment =
            BuildFragment().apply {
                arguments = Bundle().apply {
                    putString("cacheDir", cacheDir.absolutePath)
                    putString("manifestFile", manifestFile.absolutePath)
                    putString("javaSrc", javaSrc.absolutePath)
                    putString("resFolder", resFolder.absolutePath)
                    putString("outputApk", outputApk.absolutePath)
                }
            }
    }
}
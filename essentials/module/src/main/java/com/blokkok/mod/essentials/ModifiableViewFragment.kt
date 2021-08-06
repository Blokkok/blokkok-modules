package com.blokkok.mod.essentials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ModifiableViewFragment(
    // Inject onCreateView
    val injected_onCreateView: (inflater: LayoutInflater,
                                container: ViewGroup?,
                                savedInstanceState: Bundle?) -> View?
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = injected_onCreateView(inflater, container, savedInstanceState)
}
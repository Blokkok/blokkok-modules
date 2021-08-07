package com.blokkok.mod.project.manager

import android.content.res.Resources

// Used to very easily convert numbers into dp in a beautiful way
// Thanks from: https://gist.github.com/titoaesj/ccd7ddc3c40350217f2bcae248d2ffc3

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
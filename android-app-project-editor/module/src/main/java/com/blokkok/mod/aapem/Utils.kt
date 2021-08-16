package com.blokkok.mod.aapem

import android.content.res.Resources

// https://gist.github.com/titoaesj/ccd7ddc3c40350217f2bcae248d2ffc3

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
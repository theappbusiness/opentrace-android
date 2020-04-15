package com.kccreate.intelligentlockdown.extensions

import android.view.View
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment


inline fun <reified T : View> Fragment.bindView(@IdRes id: Int): Lazy<T> = lazy {
    requireView().findViewById<T>(id)
}

inline fun <reified T : Any?> Fragment.bindArgument(key: String): Lazy<T> = lazy {
    arguments?.get(key) as? T ?: throw IllegalArgumentException("Argument not passed for key: $key")
}

@Suppress("SpreadOperator")
fun <T : Fragment> T.withArguments(vararg args: Pair<String, Any?>): T {
    arguments = bundleOf(*args)
    return this
}
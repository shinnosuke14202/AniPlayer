package com.example.aniplayer.utils.fragment

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

// Keeps the existing fragment alive, just hides it.
fun Fragment.addFragment(
    @IdRes containerViewId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String? = fragment::class.java.simpleName
) {
    requireActivity().supportFragmentManager.beginTransaction().apply {
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerViewId, fragment, tag)
    }.commit()
}

// Removes the existing fragment and adds a new one.
fun Fragment.replaceFragment(
    @IdRes containerViewId: Int,
    fragment: Fragment,
    addToBackStack: Boolean,
    tag: String? = fragment::class.java.simpleName
) {
    requireActivity().supportFragmentManager.beginTransaction().apply {
        if (addToBackStack) {
            addToBackStack(tag)
        }
        replace(containerViewId, fragment, tag)
    }.commit()
}

fun Fragment.goBackFragment(): Boolean {
    requireActivity().supportFragmentManager.apply {
        val canShowPreviousPage = backStackEntryCount > 0
        if (canShowPreviousPage) {
            popBackStackImmediate()
        }
        return canShowPreviousPage
    }
}

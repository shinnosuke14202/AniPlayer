package com.example.aniplayer.utils.parcelable

import android.os.Build
import android.os.Bundle

inline fun <reified T> Bundle.parcelable(key: String): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)!!
    } else {
        @Suppress("DEPRECATION") getParcelable(key)!!
    }
}

inline fun <reified T> Bundle.parcelableArrayList(key: String): List<T> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayList(key, T::class.java)!!
    } else {
        @Suppress("DEPRECATION") getParcelableArrayList(key)!!
    }
}

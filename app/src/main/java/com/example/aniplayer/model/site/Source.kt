package com.example.aniplayer.model.site

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(
    val name: String, @DrawableRes val image: Int? = null
) : Parcelable

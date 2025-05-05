package com.example.aniplayer.model.site

import androidx.annotation.DrawableRes
import com.example.aniplayer.site.Site

data class SiteModel(val name: String, val sources: List<Site>, @DrawableRes val icon: Int? = null)

package com.example.aniplayer.utils.view

import android.content.Context
import android.content.res.Resources
import com.example.aniplayer.R

object ViewUtils {
    fun calculateCardSize(
        context: Context,
        spanCount: Int,
        heightRatio: Float = 1.8f,
        includeEdge: Boolean = true,
    ): Pair<Int, Int> {
        val displayMetrics = context.resources.displayMetrics
        val spacing: Int = context.resources.getDimensionPixelSize(R.dimen.grid_spacing)
        val totalSpacing = if (includeEdge) spacing * (spanCount + 1) else spacing * (spanCount - 1)
        val cardWidth = (displayMetrics.widthPixels - totalSpacing) / spanCount
        val cardHeight = (cardWidth * heightRatio).toInt()
        return Pair(cardWidth, cardHeight)
    }

    fun calculateSpanCount(): Int {
        val screenWidthDp =
            Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density
        return when {
            screenWidthDp > 600 -> 4 // tablets
            screenWidthDp > 400 -> 3 // medium phones
            else -> 2 // small phones
        }
    }
}

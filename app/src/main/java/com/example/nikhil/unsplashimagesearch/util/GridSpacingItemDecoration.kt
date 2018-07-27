package com.example.nikhil.unsplashimagesearch.util

import android.graphics.Rect
import android.support.v4.text.TextUtilsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View

import java.util.Locale

class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int,
                                private val includeEdge: Boolean,
                                private val headerNum: Int) : RecyclerView.ItemDecoration() {
    private val isRtl = TextUtilsCompat
            .getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) - headerNum // item position
        if (position >= 0) {
            var column = position % spanCount // item column
            if (isRtl) {
                column = spanCount - 1 - column
            }
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        } else {
            outRect.left = spacing
            outRect.right = spacing
            if (includeEdge)
                outRect.top = spacing
            else
                outRect.top = 0
            outRect.bottom = 0
        }
    }
}

package com.example.nikhil.unsplashimagesearch.util

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

class VarColumnGridLayoutManager(context: Context, spanCount:Int) :
        GridLayoutManager(context,spanCount) {

    public fun updateSpanCount(spanCount:Int){
        if (spanCount < 1)
            return
        this.spanCount = spanCount

    }
}
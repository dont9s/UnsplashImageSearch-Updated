package com.example.nikhil.unsplashimagesearch.util

import android.support.v7.widget.AppCompatImageView
import com.example.nikhil.unsplashimagesearch.model.Urls

interface ImageClickListener {
    public fun onImageClick(pos:Int, imageUrls: Urls, shareImageView: AppCompatImageView)
}
package com.example.nikhil.unsplashimagesearch.model

data class ImageModel(
        val total: Int,
        val total_pages: Int,
        val results: List<Result>
)
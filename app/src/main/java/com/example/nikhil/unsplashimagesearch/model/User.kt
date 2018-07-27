package com.example.nikhil.unsplashimagesearch.model

data class User(
        val id: String,
        val updated_at: String,
        val username: String,
        val name: String,
        val first_name: String,
        val last_name: String,
        val twitter_username: Any,
        val portfolio_url: Any,
        val bio: String,
        val location: Any,
        val links: Links,
        val profile_image: ProfileImage,
        val instagram_username: String,
        val total_collections: Int,
        val total_likes: Int,
        val total_photos: Int
)
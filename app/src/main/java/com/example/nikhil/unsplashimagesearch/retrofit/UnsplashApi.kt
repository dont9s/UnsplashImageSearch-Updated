package com.example.nikhil.unsplashimagesearch.retrofit

import com.example.nikhil.unsplashimagesearch.model.ImageModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {


    //https://api.unsplash.com/search/photos?page=1&query=office&client_id=53efca2c7e81252e8955ae316fe0e95501065518ac2de4311f8aae12a39c231f
    @GET("/search/photos")
    abstract fun getImages(
            @Query("query") query: String,
            @Query("client_id") clientId: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int):Observable<ImageModel>

}
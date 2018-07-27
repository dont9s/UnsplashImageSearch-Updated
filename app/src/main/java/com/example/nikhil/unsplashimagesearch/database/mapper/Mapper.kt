package com.example.nikhil.unsplashimagesearch.database.mapper

import com.example.nikhil.unsplashimagesearch.model.ImageModel
import com.example.nikhil.unsplashimagesearch.model.Result
import com.example.nikhil.unsplashimagesearch.model.Urls

class Mapper {
    companion object {
        public fun mapImageModelToImageUrlList(query:String, imageModel: ImageModel): ArrayList<Urls>{
            var imageUrlList:ArrayList<Urls> = ArrayList()
            for (result:Result in imageModel.results){
                result.urls.query = query
                imageUrlList.add(result.urls)
            }
            return imageUrlList
        }
    }
}
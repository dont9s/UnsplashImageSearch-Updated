package com.example.nikhil.unsplashimagesearch.database.mapper

import com.example.nikhil.unsplashimagesearch.model.ImageModel
import com.example.nikhil.unsplashimagesearch.model.Result
import com.example.nikhil.unsplashimagesearch.model.Urls
import java.util.*

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
/*2017-06-13T11:13:11-04:00*/
/*"yyyy-MM-dd'T'hh:mm:ssZ"*/
/*"yyyy-MM-dd'T'hh:mm:ss[+|-]hh:mm"*/
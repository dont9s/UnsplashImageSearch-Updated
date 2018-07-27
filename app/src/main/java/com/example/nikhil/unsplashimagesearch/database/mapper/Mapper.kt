package com.example.nikhil.unsplashimagesearch.database.mapper

import com.example.nikhil.unsplashimagesearch.model.ImageModel
import com.example.nikhil.unsplashimagesearch.model.Result
import com.example.nikhil.unsplashimagesearch.model.Urls
import com.example.nikhil.unsplashimagesearch.util.DateConverter
import java.util.*

class Mapper {
    companion object {
        public fun mapImageModelToImageUrlList(query:String, imageModel: ImageModel): ArrayList<Urls>{
            var imageUrlList:ArrayList<Urls> = ArrayList()
            for (result:Result in imageModel.results){
                result.urls.query = query
                result.urls.created_at = DateConverter
                        .stringToDate(result.created_at,
                                "yyyy-MM-dd'T'hh:mm:ss[+|-]hh:mm").time
                imageUrlList.add(result.urls)
            }
            Collections.sort(imageUrlList, object :Comparator<Urls>{
                override fun compare(p0: Urls?, p1: Urls?): Int {
                    var comparison:Int = 0
                    if (p1 != null && p0 != null) {
                        comparison =   p0.created_at.compareTo(p1.created_at)
                    }
                  return comparison
                }

            })
            return imageUrlList
        }
    }
}
/*2017-06-13T11:13:11-04:00*/
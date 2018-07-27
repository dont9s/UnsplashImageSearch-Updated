package com.example.nikhil.unsplashimagesearch.mvp

import com.example.nikhil.unsplashimagesearch.BuildConfig
import com.example.nikhil.unsplashimagesearch.model.ImageModel
import com.example.nikhil.unsplashimagesearch.retrofit.RestClient
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MainActivityModelImpl constructor(presenter: MainActivityMVP.MainActivityPresenter) :
        MainActivityMVP.MainActivityModel {

    override fun doSearch(query: String, page: Int):Observable<ImageModel> {

        return RestClient.getUnsplashApi().getImages(query,
                BuildConfig.CLIENT_ID,
                page,
                30)
                .subscribeOn(Schedulers.io())
    }

    override fun unbind() {

    }

}

package com.example.nikhil.unsplashimagesearch.mvp

import com.example.nikhil.unsplashimagesearch.model.ImageModel
import com.example.nikhil.unsplashimagesearch.model.Urls
import io.reactivex.Observable

public interface MainActivityMVP {
    public interface MainActivityView{
        fun onSearchResult(result: ArrayList<Urls>)
        fun showMessage(message: String?)
        fun showProgress()
        fun hideProgress()
        fun reset()
        fun onOffline(offline: Boolean)

    }
    public interface MainActivityPresenter{
        fun onSearch(query: String, page: Int, isLoadFromScroll: Boolean)
        fun unbind()

    }
    public interface MainActivityModel{
        fun doSearch(query: String, page: Int): Observable<ImageModel>
        fun unbind()

    }
}
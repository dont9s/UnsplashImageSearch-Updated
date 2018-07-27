package com.example.nikhil.unsplashimagesearch.mvp

import android.content.Context
import com.example.nikhil.unsplashimagesearch.database.AppDatabase
import com.example.nikhil.unsplashimagesearch.database.mapper.Mapper
import com.example.nikhil.unsplashimagesearch.model.ImageModel
import com.example.nikhil.unsplashimagesearch.model.Urls
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable


class MainActivityPresenterImpl : MainContract.MainActivityPresenter {
    private var view: MainContract.MainActivityView? = null
    private var compositeDisposable: CompositeDisposable
    private var interactor: MainContract.MainActivityInteractor? = null
    private var currentQuery: String = ""
    private val QUERY_INCORRECT_MESSAGE: String = """Query empty or repeated"""
    private var context: Context

    constructor(context: Context, view: MainContract.MainActivityView) {
        this.view = view
        this.context = context
    }

    init {
        interactor = MainActivityInteractorImpl(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun onSearch(query: String, page: Int, isLoadFromScroll: Boolean) {

        if (isLoadFromScroll) {
            view?.showProgress()
            getSearchObservableAndSendToView(currentQuery, page)
        } else if (query != null && query.length > 0 && !query.equals(currentQuery)) {
            currentQuery = query
            view?.showProgress()
            view?.reset()
            getSearchObservableAndSendToView(query, page)
        } else {
            view?.showMessage(QUERY_INCORRECT_MESSAGE)
        }
    }

    private fun getSearchObservableAndSendToView(query: String, page: Int) {
        val imageModelObservable: Observable<ImageModel>? = interactor?.doSearch(query, page)


        val disposable: Disposable? = imageModelObservable
                ?.map { t: ImageModel -> Mapper.mapImageModelToImageUrlList(currentQuery, t) }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(Consumer { result: ArrayList<Urls> ->
                    view?.hideProgress()
                    view?.onSearchResult(result)
                    view?.onOffline(false)
                    insertUrlsToDatabase(result)
                } as Consumer<in ArrayList<Urls>>?,
                        Consumer { throwable: Throwable ->
                            view?.hideProgress()
                            view?.showMessage(throwable.localizedMessage)
                            view?.onOffline(true)
                            getOfflineDataAndSet(query)
                        } as Consumer<in Throwable>
                )
        compositeDisposable.add(disposable as Disposable)
    }

    private fun getOfflineDataAndSet(query: String) {
        val disposable: Disposable? = AppDatabase.getInstance(context)
                ?.urlDao()
                ?.loadAllUrlsByQuery(query)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { cachedUrls: List<Urls>?,
                              t1: Throwable? ->
                    view?.onSearchResult(ArrayList(cachedUrls))
                }
        compositeDisposable.add(disposable as Disposable)
    }

    private fun insertUrlsToDatabase(result: ArrayList<Urls>) {


        val disposable: Disposable = Observable.fromCallable(object : Callable<Any> {
            @Throws(Exception::class)
            override fun call(): Unit? {
                return AppDatabase.getInstance(context)?.urlDao()?.insertAll(result)
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

        compositeDisposable.add(disposable)

    }

    override fun unbind() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed){
            compositeDisposable.dispose()
            interactor?.unbind()
            interactor = null
        }
    }

}

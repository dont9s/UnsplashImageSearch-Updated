package com.example.nikhil.unsplashimagesearch.retrofit

import com.example.nikhil.unsplashimagesearch.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RestClient {


    companion object {

        private var retrofit:Retrofit? = null

        public fun getUnsplashApi():UnsplashApi{
            if(retrofit == null){
                retrofit = Retrofit.Builder()
                        .baseUrl(BuildConfig.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(httpClient().build())
                        .build()
            }
            return retrofit!!.create(UnsplashApi::class.java)
        }

        private fun httpClient():OkHttpClient.Builder{
            val logging:HttpLoggingInterceptor = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient:OkHttpClient.Builder = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)
            return httpClient
        }

    }


}
package com.example.nikhil.unsplashimagesearch.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.nikhil.unsplashimagesearch.model.Urls
import com.example.nikhil.unsplashimagesearch.model.User
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface UrlDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(urlsList: ArrayList<Urls>)

    @Query("SELECT * FROM urls WHERE `query` IN (:query) ORDER BY created_at ASC")
    fun loadAllUrlsByQuery(query:String): Single<List<Urls>>


}
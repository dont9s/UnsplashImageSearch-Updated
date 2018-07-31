package com.example.nikhil.unsplashimagesearch.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.nikhil.unsplashimagesearch.model.Urls


@Database(entities = arrayOf(Urls::class), version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun urlDao(): UrlDao


    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase::class.java, "urls.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
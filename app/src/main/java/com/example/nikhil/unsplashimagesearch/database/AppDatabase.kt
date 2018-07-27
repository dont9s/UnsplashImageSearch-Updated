package com.example.nikhil.unsplashimagesearch.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.nikhil.unsplashimagesearch.model.Urls


@Database(entities = arrayOf(Urls::class), version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun urlDao(): UrlDao


    companion object {
        private var INSTANCE: AppDatabase? = null
      /*  val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Urls " + " ADD COLUMN created_at LONG")
            }
        }*/

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
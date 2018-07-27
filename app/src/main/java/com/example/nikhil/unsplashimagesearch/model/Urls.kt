package com.example.nikhil.unsplashimagesearch.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey


@Entity(indices = arrayOf(Index(value =  ["query", "raw", "full", "regular", "small", "thumb"] ,
        unique = true)))
data class Urls(

        @PrimaryKey(autoGenerate = true)
        var uid:Int,

        @ColumnInfo(name = "query")
        var query:String,

        @ColumnInfo(name = "raw")
        val raw: String,

        @ColumnInfo(name = "full")
        val full: String,

        @ColumnInfo(name = "regular")
        val regular: String,

        @ColumnInfo(name = "small")
        val small: String,

        @ColumnInfo(name = "thumb")
        val thumb: String
)
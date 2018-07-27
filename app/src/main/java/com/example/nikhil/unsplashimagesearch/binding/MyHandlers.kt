package com.example.nikhil.unsplashimagesearch.binding

import android.util.Log
import android.view.View

public class MyHandlers {
    public fun onClickSearch(view:View){
        Log.e("button_tag", view.id.toString()  + " : Hello")

    }
}
package com.example.nikhil.unsplashimagesearch.adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nikhil.unsplashimagesearch.R
import com.example.nikhil.unsplashimagesearch.model.Urls
import com.squareup.picasso.Picasso

class ImageRecyclerAdapter constructor(imageUrlList: ArrayList<Urls>, context: Context) :
        RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>() {


    private var imageUrlList: ArrayList<Urls>
    private var mInflater: LayoutInflater

    init {
        this.imageUrlList = imageUrlList
        mInflater = LayoutInflater.from(context)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item_image, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (imageUrlList != null && !imageUrlList.isEmpty()) {
            val urls: Urls = imageUrlList.get(position)
            Picasso.get()
                    .load(urls.small)
                    .into(viewHolder.ivImage)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivImage: AppCompatImageView? = null

        init {
            ivImage = itemView.findViewById(R.id.iv_image)
            ivImage?.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
        }
    }
}
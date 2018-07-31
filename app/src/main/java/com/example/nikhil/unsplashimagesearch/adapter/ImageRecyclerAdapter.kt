package com.example.nikhil.unsplashimagesearch.adapter

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nikhil.unsplashimagesearch.R
import com.example.nikhil.unsplashimagesearch.model.Urls
import com.example.nikhil.unsplashimagesearch.util.ImageClickListener
import com.squareup.picasso.Picasso

class ImageRecyclerAdapter constructor(private var imageUrlList: ArrayList<Urls>,
                                       private var context: Context,
                                       private var imageClickListener: ImageClickListener) :
        RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>() {


    private var mInflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item_image, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (imageUrlList != null && !imageUrlList.isEmpty()) {
            val urls: Urls = imageUrlList[position]
            Picasso.get()
                    .load(urls.small)
                    .into(viewHolder.ivImage)
            ViewCompat.setTransitionName(viewHolder.ivImage as View, imageUrlList[position].small)

            viewHolder.ivImage.setOnClickListener {
                imageClickListener.onImageClick(viewHolder.adapterPosition,
                        imageUrlList[position],
                        viewHolder.ivImage)
            }
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage: AppCompatImageView = itemView.findViewById(R.id.iv_image)
    }

}
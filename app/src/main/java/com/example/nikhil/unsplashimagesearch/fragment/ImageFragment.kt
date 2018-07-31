package com.example.nikhil.unsplashimagesearch.fragment

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatImageView
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nikhil.unsplashimagesearch.R
import com.example.nikhil.unsplashimagesearch.util.Constant
import com.squareup.picasso.Picasso

class ImageFragment : Fragment() {

    companion object {
        fun  newInstance(smallUrl:String, transitionName:String):ImageFragment
        {
            val imageFragment = ImageFragment()
            val bundle = Bundle()
            bundle.putString(Constant.EXTRA_IMAGE_SMALL_URL, smallUrl)
            bundle.putString(Constant.EXTRA_IMAGE_TRANSITION_NAME, transitionName)
            imageFragment.arguments = bundle
            return imageFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val ivImageSmall:AppCompatImageView = view.findViewById(R.id.iv_image_small)


        val smallUrl = arguments?.getString(Constant.EXTRA_IMAGE_SMALL_URL)
        val transitionName = arguments?.getString(Constant.EXTRA_IMAGE_TRANSITION_NAME)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivImageSmall.transitionName = transitionName
        }

        Picasso.get()
                .load(smallUrl)
                .into(ivImageSmall)
    }



}
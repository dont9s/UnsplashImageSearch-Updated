package com.example.nikhil.unsplashimagesearch

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionInflater
import android.view.View
import com.example.nikhil.unsplashimagesearch.databinding.ActivityImageBinding
import com.example.nikhil.unsplashimagesearch.fragment.ImageFragment
import com.example.nikhil.unsplashimagesearch.util.Constant


class ImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding
    private var currentImagePos: Int = -1
    private lateinit var imageUrlList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_image)
        val extras: Bundle = intent.extras
        currentImagePos = extras.getInt(Constant.EXTRA_IMAGE_INITIAL_POS)
        imageUrlList = extras.getStringArrayList(Constant.EXTRA_IMAGE_URL_LIST)

        val imagePagerAdapter = ImageViewPagerAdapter(supportFragmentManager,
                imageUrlList)
        binding.vpImage.adapter = imagePagerAdapter
        if (currentImagePos != -1)
            binding.vpImage.currentItem = currentImagePos

        binding.vpImage.addOnPageChangeListener(object :ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(position: Int) {
                currentImagePos = position
            }
        })
        prepareSharedElementTransition()
    }

    override fun finishAfterTransition() {
        val intent = Intent()
        intent.putExtra(Constant.EXTRA_IMAGE_EXIT_POS, currentImagePos)
        setResult(Activity.RESULT_OK, intent)

        super.finishAfterTransition()
    }

    /**
     * Prepares the shared element transition from and back to the grid .
     */
    private fun prepareSharedElementTransition() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.sharedElementEnterTransition = TransitionInflater
                    .from(this).inflateTransition(R.transition.change_image_transform)
        }


        // A similar mapping is set at the Grid with a setExitSharedElementCallback.
        setEnterSharedElementCallback(
                object : SharedElementCallback() {
                    override fun onMapSharedElements(names: List<String>
                                                     , sharedElements: MutableMap<String, View>) {
                        // Locate the image view at the primary fragment (the ImageFragment that is currently
                        // visible). To locate the fragment, call instantiateItem with the selection position.
                        // At this stage, the method will simply return the fragment at the position and will
                        // not create a new one.
                        val currentFragment = binding.vpImage.adapter
                                ?.instantiateItem(binding.vpImage, currentImagePos) as Fragment
                        val view = currentFragment.view ?: return

                        // Map the first shared element name to the child ImageView.
                        sharedElements[names[0]] = view.findViewById(R.id.iv_image_small)
                    }
                })
    }


    class ImageViewPagerAdapter(fm: FragmentManager, private var imageUrlList: ArrayList<String>)
        : FragmentStatePagerAdapter(fm) {

        //kept the small url to be transition name
        override fun getItem(p0: Int): Fragment {
            return ImageFragment.newInstance(imageUrlList[p0], imageUrlList[p0])
        }

        override fun getCount(): Int {
            return imageUrlList.size
        }
    }


}
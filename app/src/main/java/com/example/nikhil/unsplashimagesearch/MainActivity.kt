package com.example.nikhil.unsplashimagesearch

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.widget.Toast
import com.example.nikhil.unsplashimagesearch.adapter.ImageRecyclerAdapter
import com.example.nikhil.unsplashimagesearch.databinding.MainBinding
import com.example.nikhil.unsplashimagesearch.model.Urls
import com.example.nikhil.unsplashimagesearch.mvp.MainActivityPresenterImpl
import com.example.nikhil.unsplashimagesearch.mvp.MainContract
import com.example.nikhil.unsplashimagesearch.util.Constant
import com.example.nikhil.unsplashimagesearch.util.EndlessRecyclerViewScrollListener
import com.example.nikhil.unsplashimagesearch.util.ImageClickListener
import com.example.nikhil.unsplashimagesearch.util.VarColumnGridLayoutManager


class MainActivity : AppCompatActivity(), MainContract.MainActivityView, ImageClickListener {
    private lateinit var binding: MainBinding

    private lateinit var adapter: ImageRecyclerAdapter

    private var isOffline: Boolean = false

    private lateinit var gridLayoutManager: VarColumnGridLayoutManager

    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    private var imageUrlList: ArrayList<Urls> = ArrayList()

    private var presenter: MainContract.MainActivityPresenter? = null

    private var imageClickPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_main)
        //for initial case
        binding.indeterminateBar.visibility = View.GONE


        setupToolbar()
        setupRecyclerView()
        prepareTransitions()

        presenter = MainActivityPresenterImpl(adapter, this, this)
    }

    override fun onResume() {
        scrollToPosition()
        super.onResume()
    }

    //onSearch being called here
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar.myToolbar)
        binding.toolbar.btSearch.setOnClickListener {

            presenter?.onSearch(binding.toolbar.etSearch.text.toString().trim(),
                    1,
                    false)

        }
    }

    private fun setupRecyclerView() {
        /*initialization*/
        gridLayoutManager = VarColumnGridLayoutManager(this, 2)
        adapter = ImageRecyclerAdapter(imageUrlList, this, this)
        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!isOffline)
                    presenter?.onSearch(binding.toolbar.etSearch.text.toString().trim(),
                            page,
                            true)
            }
        }
        /*initialization ends*/

        /*setting*/
        binding.rvImages.layoutManager = gridLayoutManager

        binding.rvImages.adapter = adapter

        binding.rvImages.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
        /*setting ends*/
    }

    //here we got the result
    override fun onSearchResult(result: ArrayList<Urls>) {
        val curSize: Int = adapter.itemCount
        imageUrlList.addAll(result)
        adapter.notifyItemRangeInserted(curSize, imageUrlList.size - 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle item selection
        return when (item?.itemId) {
            R.id.mit_two_col -> {
                gridLayoutManager.updateSpanCount(2)
                true
            }
            R.id.mit_three_col -> {
                gridLayoutManager.updateSpanCount(3)
                true
            }
            R.id.mit_four_col -> {
                gridLayoutManager.updateSpanCount(4)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun showProgress() {
        if (binding.indeterminateBar.visibility == View.GONE)
            binding.indeterminateBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        if (binding.indeterminateBar.visibility == View.VISIBLE)
            binding.indeterminateBar.visibility = View.GONE
    }

    override fun showMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun reset() {
        if (imageUrlList != null && imageUrlList.size > 0) {
            imageUrlList.clear()
            adapter.notifyDataSetChanged()
            if (scrollListener != null)
                scrollListener?.setCurrentPage(1)
        }
    }

    override fun onOffline(offline: Boolean) {
        isOffline = offline
    }

    override fun onImageClick(pos: Int, imageUrls: Urls, shareImageView: AppCompatImageView) {
        imageClickPosition = pos
        val intent = Intent(this, ImageActivity::class.java)

        intent.putExtra(Constant.EXTRA_IMAGE_URL_LIST, getSmallUrlStringListFromDataSet())
        intent.putExtra(Constant.EXTRA_IMAGE_TRANSITION_NAME,
                ViewCompat.getTransitionName(shareImageView))
        intent.putExtra(Constant.EXTRA_IMAGE_INITIAL_POS, pos)
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                shareImageView,
                ViewCompat.getTransitionName(shareImageView)!!)


        startActivity(intent, options.toBundle())
    }

    private fun getSmallUrlStringListFromDataSet(): ArrayList<String> {
        val smallStringList: ArrayList<String> = ArrayList()
        for (urls: Urls in imageUrlList) {
            smallStringList.add(urls.small)
        }
        return smallStringList
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null)
            imageClickPosition = data.getIntExtra(Constant.EXTRA_IMAGE_EXIT_POS,
                    imageClickPosition)
    }

    /**
     * Prepares the shared element transition to the pager fragment, as well as the other transitions
     * that affect the flow.
     */
    private fun prepareTransitions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.exitTransition = (TransitionInflater.from(this)
                    .inflateTransition(R.transition.change_image_transform))
        }

        // A similar mapping is set at the ImageActivity with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
                object : SharedElementCallback() {
                    override fun onMapSharedElements(names: List<String>,
                                                     sharedElements: MutableMap<String, View>) {
                        // Locate the ViewHolder for the clicked position.

                        val selectedViewHolder = binding.rvImages
                                .findViewHolderForAdapterPosition(imageClickPosition)
                        if (selectedViewHolder?.itemView == null) {
                            return
                        }

                        // Map the first shared element name to the child ImageView.
                        sharedElements[names[0]] = selectedViewHolder
                                .itemView.findViewById(R.id.iv_image)
                    }
                })
    }

    /**
     * Scrolls the recycler view to show the last viewed item in the grid. This is important when
     * navigating back from the grid.
     */
    private fun scrollToPosition() {
        binding.rvImages.addOnLayoutChangeListener(object : OnLayoutChangeListener {
            override fun onLayoutChange(v: View,
                                        left: Int,
                                        top: Int,
                                        right: Int,
                                        bottom: Int,
                                        oldLeft: Int,
                                        oldTop: Int,
                                        oldRight: Int,
                                        oldBottom: Int) {
                binding.rvImages.removeOnLayoutChangeListener(this)
                val layoutManager = binding.rvImages.layoutManager
                val viewAtPosition = layoutManager?.findViewByPosition(imageClickPosition)
                // Scroll to position if the view for the current position is null
                // (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                                .isViewPartiallyVisible(viewAtPosition
                                        , false
                                        , true)) {
                    binding.rvImages.post { layoutManager?.scrollToPosition(imageClickPosition) }
                }
            }
        })
    }

    override fun onDestroy() {
        if (presenter != null)
            presenter?.unbind()
        scrollListener = null
        presenter = null
        super.onDestroy()
    }

}

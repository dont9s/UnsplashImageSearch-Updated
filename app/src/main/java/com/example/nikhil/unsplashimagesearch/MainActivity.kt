package com.example.nikhil.unsplashimagesearch

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.nikhil.unsplashimagesearch.adapter.ImageRecyclerAdapter
import com.example.nikhil.unsplashimagesearch.databinding.ActivityMainBinding
import com.example.nikhil.unsplashimagesearch.model.Urls
import com.example.nikhil.unsplashimagesearch.mvp.MainActivityMVP
import com.example.nikhil.unsplashimagesearch.mvp.MainActivityPresenterImpl
import com.example.nikhil.unsplashimagesearch.util.EndlessRecyclerViewScrollListener
import com.example.nikhil.unsplashimagesearch.util.VarColumnGridLayoutManager

class MainActivity : AppCompatActivity(), MainActivityMVP.MainActivityView {
    lateinit var binding: ActivityMainBinding

    lateinit var adapter: ImageRecyclerAdapter

    var isOffline: Boolean = false

    lateinit var gridLayoutManager: VarColumnGridLayoutManager

    lateinit var scrollListener: EndlessRecyclerViewScrollListener

    var imageUrlList: ArrayList<Urls> = ArrayList()

    private var presenter: MainActivityMVP.MainActivityPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_main)
        //for initial case
        binding.indeterminateBar.visibility = View.GONE

        presenter = MainActivityPresenterImpl(this, this)
        setupToolbar()
        setupRecyclerView()

    }

    override fun onDestroy() {
        super.onDestroy()
        if (presenter != null)
            presenter?.unbind()

        presenter = null
    }

    //onSearch being called here
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar!!.myToolbar)
        binding.toolbar!!.btSearch!!.setOnClickListener(View.OnClickListener { it: View ->

            presenter?.onSearch(binding.toolbar?.etSearch?.text.toString().trim(),
                    1,
                    false)

        })
    }

    private fun setupRecyclerView() {
        /*initialization*/
        gridLayoutManager = VarColumnGridLayoutManager(this, 2)
        adapter = ImageRecyclerAdapter(imageUrlList, this)
        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!isOffline)
                    presenter?.onSearch(binding.toolbar?.etSearch?.text.toString().trim(),
                            page,
                            true)
            }
        }
        /*initialization ends*/

        /*setting*/
        binding.rvImages.layoutManager = gridLayoutManager

        binding.rvImages.adapter = adapter

        binding.rvImages.addOnScrollListener(scrollListener)
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
        when (item?.getItemId()) {
            R.id.mit_two_col -> {
                gridLayoutManager.updateSpanCount(2)
                return true
            }
            R.id.mit_three_col -> {
                gridLayoutManager.updateSpanCount(3)
                return true
            }
            R.id.mit_four_col -> {
                gridLayoutManager.updateSpanCount(4)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
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
                scrollListener.setCurrentPage(1)
        }
    }

    override fun onOffline(offline: Boolean) {
        isOffline = offline
    }
}

package com.gravityloft.androidassignment.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gravityloft.androidassignment.R
import com.gravityloft.androidassignment.`interface`.RecyclerviewInteractionListener
import com.gravityloft.androidassignment.adapter.RecyclerViewAdapter
import com.gravityloft.androidassignment.databinding.ActivityMainBinding
import com.gravityloft.androidassignment.model.Item
import com.gravityloft.androidassignment.repository.Response
import com.gravityloft.androidassignment.viewmodels.MainViewModel
import com.gravityloft.androidassignment.viewmodels.MainViewModelFactory
import java.io.Serializable


class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityMainBinding

    // VISIBLE -> 0  , // GONE = 8
    private val VISIBLE_: Int = 0
    private val VISIBILITY_GONE_: Int = 8

    //Variable for checking progressbar loading or not
    lateinit var layoutManager: LinearLayoutManager


    // isScrolling
    private var isScrolling: Boolean = false
    private var currentItem: Int = 0
    private var totalItem: Int = 0
    private var scrollOuttItems: Int = 0


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val repository = (application as MainApplication).dataRepository


        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository, application)
        ).get(MainViewModel::class.java)


        initRecyclerView()
        mainViewModel.dataList.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    binding.progressbar.visibility = VISIBLE_
                }
                is Response.Success -> {
                    it.data?.let {
                        recyclerViewAdapter.setListData(mainViewModel.getItemlist())
                        recyclerViewAdapter.notifyDataSetChanged()
                        binding.progressbar.visibility = VISIBILITY_GONE_
                    }
                }
                is Response.Error -> {
                    binding.progressbar.visibility = VISIBILITY_GONE_
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        })




        if (mainViewModel.getItemlist().size > 0) {
            recyclerViewAdapter.setListData(mainViewModel.getItemlist())
        }


        if (mainViewModel.isLoading()) {
            binding.progressbar.visibility = VISIBLE_
        } else {
            binding.progressbar.visibility = VISIBILITY_GONE_
        }
    }

    fun initRecyclerView() {

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.mRecycler.layoutManager = layoutManager

        binding.mRecycler.isNestedScrollingEnabled = false
        binding.mRecycler.apply {
            recyclerViewAdapter = RecyclerViewAdapter(clickListener = object :
                RecyclerviewInteractionListener {
                override fun onItemSelected(position: Int, item: Any, clickItemType: Int?) {
                    item as Item
                    val intent = Intent(this@MainActivity, DetailDataActivity::class.java)
                    intent.putExtra("item", item as Serializable)
                    startActivity(intent)
                }
            })
            adapter = recyclerViewAdapter

        }


        binding.mRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                currentItem = layoutManager.childCount
                totalItem = layoutManager.itemCount
                scrollOuttItems = layoutManager.findFirstVisibleItemPosition()

                if (!mainViewModel.isLoading()) {
                    if (isScrolling && (currentItem + scrollOuttItems == totalItem)) {
                        isScrolling = false
                        mainViewModel.loadMore()
                    }
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })

    }


}
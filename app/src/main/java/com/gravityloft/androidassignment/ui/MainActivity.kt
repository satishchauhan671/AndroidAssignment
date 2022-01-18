package com.gravityloft.androidassignment.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.gravityloft.androidassignment.utils.AppPreferenceHelper
import com.gravityloft.androidassignment.viewmodels.MainViewModel
import com.gravityloft.androidassignment.viewmodels.MainViewModelFactory
import java.io.Serializable


class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var arrayList: ArrayList<Item>
    private var page: Int = 0
    lateinit var appPreferenceHelper: AppPreferenceHelper


    // VISIBLE -> 0  , // GONE = 8
    private val VISIBLE_ : Int = 0
    private val GONE_ : Int = 8

    //Variable for checking progressbar loading or not
    private var isLoading: Boolean = false
    lateinit var layoutManager: LinearLayoutManager


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        arrayList = ArrayList()
        val repository = (application as MainApplication).dataRepository

        // initialize preference helper
        appPreferenceHelper = AppPreferenceHelper(this)

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository, this)).get(
            MainViewModel::class.java
        )


        mainViewModel.dataList.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    binding.progressbar.visibility = VISIBLE_
                    isLoading = true
                }
                is Response.Success -> {

                    it.data?.let {
                        arrayList.addAll(it.items)
                        recyclerViewAdapter.setListData(arrayList)
                        isLoading = false

                        Log.d(TAG, "onCreate: size"+it.items.size)
                    }
                    binding.progressbar.visibility = GONE_
                    recyclerViewAdapter.notifyDataSetChanged()
                }
                is Response.Error -> {
                    isLoading = false
                    binding.progressbar.visibility = GONE_
                }
            }
        })



        initRecyclerView()

    }

    fun initRecyclerView() {

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.mRecycler.layoutManager = layoutManager

        binding.mRecycler.isNestedScrollingEnabled = false
        binding.mRecycler.apply {
            recyclerViewAdapter = RecyclerViewAdapter(clickListener = object :
                RecyclerviewInteractionListener {
                override fun onItemSelected(position: Int, item: Any, clickItemType: Int?) {
                    Log.d("onItemSelected: ", item.toString())
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

                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == arrayList.size - 1) {
                        Log.d(
                            TAG,
                            "onScrolled: Size: ${arrayList.size} " + "page " + page
                        )
                        mainViewModel.loadPageData(page++, this@MainActivity)
                    }
                }

            }
        })

    }


}
package com.gravityloft.androidassignment.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gravityloft.androidassignment.model.DataPageList
import com.gravityloft.androidassignment.model.Item
import com.gravityloft.androidassignment.repository.DataRepository
import com.gravityloft.androidassignment.repository.Response
import com.gravityloft.androidassignment.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataRepository, private val context: Context) :
    ViewModel() {

    private var page = 0

    init {
        if (NetworkUtils.isInternetAvailable(context)) {


            loadPageData(page)

        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }

    }

    val dataList: LiveData<Response<DataPageList>>
        get() = repository.dataList


    private fun loadPageData(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDataList(page)
        }
    }

    fun loadMore() {
        loadPageData(page++)
    }

    fun isLoading(): Boolean {
        return repository.isLoading()
    }

    fun getItemlist(): ArrayList<Item> {
        return repository.getList()
    }

}
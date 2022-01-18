package com.gravityloft.androidassignment.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gravityloft.androidassignment.model.DataPageList
import com.gravityloft.androidassignment.repository.DataRepository
import com.gravityloft.androidassignment.repository.Response
import com.gravityloft.androidassignment.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.log

class MainViewModel(private val repository: DataRepository, private val context: Context,private val page: Int=0) :
    ViewModel() {

    init {
        if (NetworkUtils.isInternetAvailable(context)) {

               viewModelScope.launch(Dispatchers.IO) {
                   loadPageData(page,context)
                }
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }

    }

    val dataList: LiveData<Response<DataPageList>>
        get() = repository.dataList


    fun loadPageData(page: Int,context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDataList(page, context)
        }
    }

}
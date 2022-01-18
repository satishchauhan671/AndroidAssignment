package com.gravityloft.androidassignment.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gravityloft.androidassignment.api.DataService
import com.gravityloft.androidassignment.model.DataPageList
import com.gravityloft.androidassignment.repository.Response.Loading
import com.gravityloft.androidassignment.utils.NetworkUtils

class DataRepository(
        private val dataService: DataService,
        private val applicationContext: Context
) {

    private val dataLiveData = MutableLiveData<Response<DataPageList>>()

    val dataList: LiveData<Response<DataPageList>>
    get() = dataLiveData

    suspend fun getDataList(page: Int, context: Context){

        if(NetworkUtils.isInternetAvailable(applicationContext)){
            try {
                dataLiveData.postValue(Loading(true))
                val result = dataService.getPageList(page)
            if(result.body() != null){
                dataLiveData.postValue(Loading(false))
                dataLiveData.postValue(Response.Success(result.body()))
            }
            }catch (e : Exception){
                dataLiveData.postValue(Loading(false))
                dataLiveData.postValue(Response.Error(e.message.toString()))
            }
        }else{
            dataLiveData.postValue(Loading(false))
        }

    }



}








package com.gravityloft.androidassignment.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gravityloft.androidassignment.api.DataService
import com.gravityloft.androidassignment.model.DataPageList
import com.gravityloft.androidassignment.model.Item
import com.gravityloft.androidassignment.repository.Response.Loading
import com.gravityloft.androidassignment.utils.NetworkUtils
import okhttp3.ResponseBody
import org.json.JSONObject


class DataRepository(
    private val dataService: DataService,
    private val applicationContext: Context
) {

    private val dataLiveData = MutableLiveData<Response<DataPageList>>()

    val dataList: LiveData<Response<DataPageList>>
        get() = dataLiveData
    private val dataItem = ArrayList<Item>()
    private var isloading: Boolean = false

    suspend fun getDataList(page: Int) {

        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            try {
                dataLiveData.postValue(Loading(true))
                isloading = true
                val result = dataService.getPageList(page)
                if (result.body() != null) {
                    dataLiveData.postValue(Loading(false))
                    dataLiveData.postValue(Response.Success(result.body()))
                    dataItem.addAll(result.body()!!.items)
                    isloading = false
                } else {
                    dataLiveData.postValue(Loading(false))
                    isloading = false
                    val errorMessage: String
                    errorMessage = result.errorBody()?.let { getErrorMessage(it) }!!
                    dataLiveData.postValue(Response.Error(errorMessage))
                }
            } catch (e: Exception) {
                dataLiveData.postValue(Loading(false))
                dataLiveData.postValue(Response.Error(e.message.toString()))
                isloading = false
                Log.d("TAGGGGG", "getDataListExx: " + dataItem.size)
            }
        } else {
            dataLiveData.postValue(Loading(false))
            isloading = false
            dataLiveData.postValue(Response.Error("No Internet Connection"))
            Log.d("TAGGGGG", "getDataListErr: " + dataItem.size)
        }

    }

    fun getList(): ArrayList<Item> {
        return dataItem
    }

    fun isLoading(): Boolean {
        return isloading
    }

    private fun getErrorMessage(responseBody: ResponseBody): String {
        var errorMessage: String

        try {
            val jObjError = JSONObject(responseBody.string())
            errorMessage = jObjError.getString("message")
        } catch (e: java.lang.Exception) {
            errorMessage = e.message.toString()
        }
        Log.d("TAGGGGG", "getErrorMessage: " + errorMessage)
        return errorMessage
    }

}








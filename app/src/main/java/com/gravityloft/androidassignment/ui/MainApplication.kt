package com.gravityloft.androidassignment.ui

import android.app.Application
import android.widget.Toast
import com.gravityloft.androidassignment.api.DataService
import com.gravityloft.androidassignment.api.RetrofitHelper
import com.gravityloft.androidassignment.repository.DataRepository

class MainApplication : Application() {

    lateinit var dataRepository: DataRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val dataService = RetrofitHelper.getInstance().create(DataService::class.java)
        dataRepository = DataRepository(dataService, applicationContext)
    }

}
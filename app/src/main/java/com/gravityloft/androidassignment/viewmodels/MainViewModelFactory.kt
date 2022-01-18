package com.gravityloft.androidassignment.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gravityloft.androidassignment.repository.DataRepository

class MainViewModelFactory(private val repository: DataRepository, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository, context ) as T
    }
}
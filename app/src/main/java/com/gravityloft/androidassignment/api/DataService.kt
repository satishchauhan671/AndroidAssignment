package com.gravityloft.androidassignment.api

import com.gravityloft.androidassignment.model.DataPageList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DataService {

    @GET("/search/code?q=addClass+user:mozilla&per_page=10")
    suspend fun getPageList(@Query("page") page: Int) : Response<DataPageList>

}
package com.gravityloft.androidassignment.repository

sealed class Response<T>(val data: T?= null, val errorMessage: String? = null, val boolean: Boolean = false){
    class Loading<T>(boolean: Boolean) : Response<T>(boolean = boolean)
    class Success<T>( data : T?= null) : Response<T>(data = data)
    class Error<T>( errorMessage : String) : Response<T>(errorMessage = errorMessage)
}

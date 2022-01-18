package com.gravityloft.androidassignment.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class AppPreferenceHelper(context: Context) {

    private var mPrefs: SharedPreferences? = null
    public  val LAST_PAGE : String = "LAST_PAGE"

    init {
        Log.e("AppPreferenceHelper", "Invoked")
        initPref(context)
    }


    private fun initPref(context: Context) {
        if (mPrefs == null)
            mPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    }

    fun saveIntPref(key: String, value: Int) {
        val editor = mPrefs!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }
    fun getIntPref(key: String): Int {
        return mPrefs!!.getInt(key, 0)
    }

    /*
     clear all preferences
     */
    fun clearAllPref(){
        val editor = mPrefs?.edit()
        editor?.clear()
        editor?.apply()
    }
}
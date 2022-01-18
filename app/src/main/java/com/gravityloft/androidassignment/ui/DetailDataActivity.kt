package com.gravityloft.androidassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.gravityloft.androidassignment.R
import com.gravityloft.androidassignment.databinding.ActivityDetailDataBinding
import com.gravityloft.androidassignment.model.Item

class DetailDataActivity : AppCompatActivity() {

    lateinit var detailDataBinding: ActivityDetailDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail_data)


            val item = intent.getSerializableExtra("item") as Item


        detailDataBinding.repoModel = item.repository
        detailDataBinding.ownerModel = item.repository.owner

    }
}
package com.gravityloft.androidassignment.model

import java.io.Serializable

data class Item(
    val git_url: String,
    val html_url: String,
    val name: String,
    val path: String,
    val repository: Repository,
    val score: Double,
    val sha: String,
    val url: String
) : Serializable
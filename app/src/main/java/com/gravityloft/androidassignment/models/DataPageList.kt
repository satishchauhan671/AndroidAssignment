package com.gravityloft.androidassignment.model

data class DataPageList(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)
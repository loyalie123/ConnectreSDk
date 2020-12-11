package com.loyalie.connectre.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginatingScrollListener1(val linearLayoutMangr: LinearLayoutManager,val recordPerPage : Int) : RecyclerView.OnScrollListener() {
    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    private val visibleThreshold = 5 // The minimum amount of items to have below your current scroll position before loading more.
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var offset = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = linearLayoutMangr.itemCount
        firstVisibleItem = linearLayoutMangr.findFirstVisibleItemPosition()
        if (dy <= 0 || totalItemCount < recordPerPage+1) return

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
                       }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {

                offset += recordPerPage
                loading = true
                onLoadMore(offset)


        }
    }

    abstract fun onLoadMore(offset: Int)
    fun reset(previousTotal: Int, loading: Boolean) {
        this.previousTotal = previousTotal
        this.loading = loading
        offset = 0
    }

    fun resetLoading() {
        loading = false
        offset -= recordPerPage
    }

}
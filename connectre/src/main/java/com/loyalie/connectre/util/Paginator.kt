package com.loyalie.connectre.util

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.loyalie.connectre.R
import kotlinx.android.synthetic.main.loader_horizonta_scroll.view.*
import kotlinx.android.synthetic.main.loader_item.view.*

const val PAGINATOR_ITEMS_PER_PAGE = 3
abstract class PaginatingScrollListener(
    var linearLayoutManager: LinearLayoutManager? = null,
    var gridLayoutManager: GridLayoutManager? = null,
    var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null,
    val pageSize: Int = PAGINATOR_ITEMS_PER_PAGE,
    val direction: Int = RecyclerView.VERTICAL
) : RecyclerView.OnScrollListener() {


    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    private val visibleThreshold =5 // The minimum amount of items to have below your current scroll position before loading more.
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var offset = 0
    private var lastCompleteleyVisibleItemPosition = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount

        linearLayoutManager?.let {

            totalItemCount = it.itemCount
            firstVisibleItem = it.findFirstVisibleItemPosition()
            lastCompleteleyVisibleItemPosition = it.findLastCompletelyVisibleItemPosition()

        }

        staggeredGridLayoutManager?.let {
            totalItemCount = it.itemCount
            firstVisibleItem = it.findFirstVisibleItemPositions(null).get(0)
            lastCompleteleyVisibleItemPosition =
                it.findFirstCompletelyVisibleItemPositions(null).get(0)

        }

        gridLayoutManager?.let {
            totalItemCount = it.itemCount
            firstVisibleItem = it.findFirstVisibleItemPosition()
            lastCompleteleyVisibleItemPosition = it.findLastCompletelyVisibleItemPosition()
        }
        if (direction == RecyclerView.VERTICAL && (dy <= 0 || totalItemCount < pageSize + 1)) return
        else if (direction == RecyclerView.HORIZONTAL && dx <= 0 || totalItemCount < pageSize + 1) return

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }


        if (!loading && (lastCompleteleyVisibleItemPosition >= (totalItemCount - visibleItemCount))) {
            loading = true
            onLoadMore()
        }
    }

    abstract fun onLoadMore()

    fun reset(previousTotal: Int, loading: Boolean) {
        this.previousTotal = previousTotal
        this.loading = loading
        offset = 0
    }

    fun setLoading(load: Boolean) {
        loading = load
    }

}


abstract class PaginatingAdapter<T>(
    val context: Context, private val mItems: List<T>,
    val direction: Int = RecyclerView.VERTICAL
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var showLoader: Boolean = false
    private val VIEWTYPE_ITEM = 1
    private val VIEWTYPE_LOADER = 2

    var mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEWTYPE_LOADER) {
            if (direction == RecyclerView.HORIZONTAL) {
                val view = mInflater.inflate(R.layout.loader_horizonta_scroll, viewGroup, false)
                return LoaderHorizontalViewHolder(view, context)
            } else {
                val view = mInflater.inflate(R.layout.loader_item, viewGroup, false)
                return LoaderViewHolder(view, context)
            }

        } else if (viewType == VIEWTYPE_ITEM) {
            return getYourItemViewHolder(viewGroup)
        }

        throw IllegalArgumentException("Invalid ViewType: " + viewType)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        // Loader ViewHolder
        if (viewHolder is LoaderViewHolder || viewHolder is LoaderHorizontalViewHolder) {
            if (showLoader) {
                if (direction == RecyclerView.HORIZONTAL) viewHolder.itemView.loader_hor?.visibility =
                    View.VISIBLE
                else viewHolder.itemView.loader?.visibility = View.VISIBLE
            } else {
                if (direction == RecyclerView.HORIZONTAL) viewHolder.itemView.loader_hor?.visibility =
                    View.GONE
                else viewHolder.itemView.loader?.visibility = View.GONE
            }

            return
        }

        bindYourViewHolder(viewHolder, position)

    }

    override fun getItemCount(): Int {
        return if (mItems.isEmpty()) 0
        else mItems.size + 1

    }

    override fun getItemViewType(position: Int): Int {
        return if (position != 0 && position == itemCount - 1) {
            VIEWTYPE_LOADER
        } else VIEWTYPE_ITEM

    }

    fun showLoading(status: Boolean) {
        showLoader = status
        notifyItemChanged(mItems.size)
    }


    abstract fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    class LoaderViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {


        init {
            itemView.loader?.indeterminateDrawable?.setColorFilter(
                ConnectReApp.themeColor, PorterDuff.Mode.MULTIPLY
            )
        }


    }

    class LoaderHorizontalViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {


        init {
            itemView.loader_hor?.indeterminateDrawable?.setColorFilter(
                ConnectReApp.themeColor, PorterDuff.Mode.MULTIPLY
            )
        }
    }
}




package com.loyalie.connectre.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.BannerImageItem
import com.loyalie.connectre.util.loadUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_banner_bottom.view.*


class BottomBannerAdapter(
    private var projects: List<BannerImageItem>,
    private val picasso: Picasso,

    context: Context
) : RecyclerView.Adapter<BottomBannerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_banner_bottom, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return projects.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }


    fun MoviesAdapter(moviesList: List<BannerImageItem>) {
        this.projects = moviesList
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        projects.get(position).imageName.let {
            holder.itemView.bannerIV.loadUrl(it, picasso)
        } ?: run {
            holder.itemView.bannerIV.loadUrl("", picasso)
        }
    }
}
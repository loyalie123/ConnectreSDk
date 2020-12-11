package com.loyalie.connectre.ui.project.location

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.AdditionalHighlightItem
import com.loyalie.connectre.util.ConnectReApp
import kotlinx.android.synthetic.main.location_heading_item.view.*

class LocationHeaderAdapter(
    val headingList: List<AdditionalHighlightItem>,
    val context: Context
) : RecyclerView.Adapter<LocationHeaderAdapter.LocationHeaderVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHeaderVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_heading_item, parent, false)
        v.tickIV.setColorFilter(ConnectReApp.themeColor)
        return LocationHeaderVH(v)
    }

    override fun getItemCount(): Int {
        return headingList.size
    }


    override fun onBindViewHolder(holder: LocationHeaderVH, position: Int) {
        holder.bind(headingList[position])
    }

    inner class LocationHeaderVH(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(item: AdditionalHighlightItem) {
            itemView.locationHeadingTV.text = item.pahTitle
            itemView.subtitleTV.text = item.pahDescription
        }
    }


}
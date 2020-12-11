package com.loyalie.connectre.ui.project.location

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.util.ConnectReApp
import kotlinx.android.synthetic.main.location_sub_heading_item.view.*

class LocationSubHeaderAdapter(private val context: Context) :
    RecyclerView.Adapter<LocationSubHeaderAdapter.LocationHeaderVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHeaderVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_sub_heading_item, parent, false)
        return LocationHeaderVH(v)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: LocationHeaderVH, position: Int) {

    }

    inner class LocationHeaderVH(v: View) : RecyclerView.ViewHolder(v) {

        init {
            itemView.tickIV.setColorFilter(ConnectReApp.themeColor)
        }

    }
}
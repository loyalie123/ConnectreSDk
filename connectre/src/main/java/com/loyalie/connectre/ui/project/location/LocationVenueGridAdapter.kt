package com.loyalie.connectre.ui.project.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.HighlightDetail
import kotlinx.android.synthetic.main.venue_grid_item.view.*

class LocationVenueGridAdapter(val venueList: List<HighlightDetail>) :
    RecyclerView.Adapter<LocationVenueGridAdapter.LocationVenueVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationVenueVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.venue_grid_item, parent, false)
        return LocationVenueVH(v)
    }

    override fun getItemCount(): Int {
        return venueList.size
    }

    override fun onBindViewHolder(holder: LocationVenueVH, position: Int) {
        holder.bind(venueList[position])
    }

    inner class LocationVenueVH(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(item: HighlightDetail) {
            itemView.venueTitleTV.text = item.title
            itemView.venueDistanceTV.text = item.description

        }
    }
}
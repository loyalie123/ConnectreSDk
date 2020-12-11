package com.loyalie.connectre.ui.project.floor_plan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.GalleryItem
import com.loyalie.connectre.ui.image_gallery.ImagePagerActivity
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.loadUrlWithPh
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.floor_plan_item.view.*

class FloorPlanAdapter(
    context: Context,
    private val gallery: List<GalleryItem>,
    private val picasso: Picasso
) : PaginatingAdapter<GalleryItem>(context, gallery) {

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.floor_plan_item, parent, false)
        return FloorPlanVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FloorPlanVH) holder.bind(gallery[position])
    }


    inner class FloorPlanVH(v: View) : RecyclerView.ViewHolder(v) {
        init {
            itemView.setOnClickListener {
                ImagePagerActivity.start(
                    it.context,
                    gallery as ArrayList<GalleryItem>,
                    adapterPosition
                )
            }
        }

        fun bind(item: GalleryItem) {
            itemView.floorPlanIV.loadUrlWithPh(item.url, picasso)
        }
    }
}
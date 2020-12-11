package com.loyalie.connectre.ui.project.construction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.GalleryItem
import com.loyalie.connectre.ui.image_gallery.ImagePagerActivity
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.getGreyPlaceHolder
import com.loyalie.connectre.util.loadUrlWithPh
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.construction_progress_grid_item.view.*

class ConstructionProgressAdapter(
    context: Context,
    private val gallery: List<GalleryItem>,
    private val picasso: Picasso
) : PaginatingAdapter<GalleryItem>(context, gallery) {

    private val placeholder by lazy { context.getGreyPlaceHolder() }

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.construction_progress_grid_item, parent, false)
        return ConstructionVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ConstructionVH) holder.bind(gallery[position])
    }


    inner class ConstructionVH(v: View) : RecyclerView.ViewHolder(v) {
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
            itemView.constructionProgressIV.loadUrlWithPh(item.url, picasso)
        }

    }
}
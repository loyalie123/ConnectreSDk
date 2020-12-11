package com.loyalie.connectre.ui.project.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.GalleryItem
import com.loyalie.connectre.ui.image_gallery.ImagePagerActivity
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.loadUrlWithPh
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.gallery_item.view.*

class GalleyAdapter(
    context: Context,
    private val galleryItems: List<GalleryItem>,
    private val picasso: Picasso
) : PaginatingAdapter<GalleryItem>(context, galleryItems) {

    private val set = ConstraintSet()

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return GalleryViewHolder(view)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GalleryViewHolder) holder.bind()
    }


    inner class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mConstraintLayout: ConstraintLayout = itemView.findViewById(R.id.parentCL)
        val galleryIV: ImageView = itemView.findViewById(R.id.galleryIV)

        init {
            itemView.setOnClickListener {
                ImagePagerActivity.start(
                    itemView.context,
                    galleryItems as ArrayList<GalleryItem>,
                    adapterPosition
                )
            }
        }

        fun bind() {
            with(galleryItems[adapterPosition]) {

                galleryIV.loadUrlWithPh(url, picasso)
                if (tagLine != "") {
                    itemView.tagTv.visibility = View.VISIBLE
                    itemView.tagTv.text = tagLine
                } else {
                    itemView.tagTv.visibility = View.GONE
                }
                kotlin.with(set) {
                    @SuppressLint("DefaultLocale")
                    val posterRatio = kotlin.String.format("%d:%d", width, height)
                    clone(mConstraintLayout)
                    setDimensionRatio(galleryIV.id, posterRatio)
                    applyTo(mConstraintLayout)
                }
            }
        }

    }
}

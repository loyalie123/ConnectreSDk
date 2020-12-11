package com.loyalie.connectre.ui.image_gallery

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager.widget.PagerAdapter
import com.github.chrisbanes.photoview.PhotoView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.GalleryItem
import com.squareup.picasso.Picasso

class ImageGalleryAdapter(
    private val context: Activity, private val imgArray: List<GalleryItem>,
    val listener: OnTapListener,
    val picasso: Picasso
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    override fun getCount(): Int {
        return imgArray.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as FrameLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View?
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        itemView = layoutInflater?.inflate(R.layout.image_gallery_single_item, container, false)
        val coverPic = itemView?.findViewById(R.id.galleryIV) as PhotoView
        if (imgArray[position].url.isNullOrBlank()) coverPic.setImageResource(R.drawable.placeholder)
        else picasso.load(imgArray[position].url).fit().centerInside().placeholder(R.drawable.placeholder).into(
            coverPic
        )

        container.addView(itemView)

        itemView.setOnClickListener {
            listener.onTapped(position)
        }

        return itemView

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as FrameLayout)
    }

    interface OnTapListener {
        fun onTapped(position: Int)
    }
}

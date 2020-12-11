package com.loyalie.connectre.ui.project.details

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.loyalie.connectre.R
import com.loyalie.connectre.data.GalleryItem
import com.loyalie.connectre.ui.image_gallery.ImagePagerActivity
import com.loyalie.connectre.util.getGreyPlaceHolder
import com.loyalie.connectre.util.loadUrlWithPh
import com.squareup.picasso.Picasso

class DetailsImgPageAdapter(
    private val images: List<GalleryItem>,
    private val picasso: Picasso,
    private val context: Context
) : PagerAdapter() {

    private val placeholder by lazy { context.getGreyPlaceHolder() }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(view.context)
            .inflate(R.layout.project_detail_image_item, view, false)
        itemView.findViewById<ImageView>(R.id.bannerIV).loadUrlWithPh(images[position].url, picasso)
        view.addView(itemView)
        itemView.setOnClickListener {
            ImagePagerActivity.start(view.context, images as ArrayList<GalleryItem>, position)
        }
        return itemView
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }
}
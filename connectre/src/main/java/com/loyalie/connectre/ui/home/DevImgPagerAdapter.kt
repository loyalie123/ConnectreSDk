package com.loyalie.connectre.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.loyalie.connectre.R
import com.loyalie.connectre.data.BannerImageItem
import com.loyalie.connectre.util.loadUrlWithPh
import com.squareup.picasso.Picasso

class DevImgPagerAdapter(
    private val images: List<BannerImageItem>,
    private val picasso: Picasso,
    private val context: Context
) : PagerAdapter() {

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
        val view1 = LayoutInflater.from(view.context)
            .inflate(R.layout.dev_image_item, view, false)
        val mBannerIV = view1.findViewById(R.id.bannerIV) as ImageView
        val image = images[position].imageName
        mBannerIV.loadUrlWithPh(image, picasso)
        view1.setOnClickListener {
            //ImagePagerActivity.start(view.context,
            //images.map { GalleryItem(it.imageName, "", "", 0, 0) } as ArrayList<GalleryItem>,
            //position)
        }
        view.addView(view1)
        return view1
    }
}
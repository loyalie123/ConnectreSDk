package com.loyalie.connectre.ui.project.testimonial

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.TestimonialItem
import com.loyalie.connectre.util.PaginatingAdapter
import com.squareup.picasso.Picasso

class TestimonialAdapter(
    context: Context,
    private val testimonials: List<TestimonialItem>,
    private val picasso: Picasso
) :
    PaginatingAdapter<TestimonialItem>(context, testimonials) {

    val placeholder by lazy {
        ColorDrawable(ContextCompat.getColor(context, R.color.textColorGreyAlpha30))
    }


    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.testimonial_row_item, parent, false)
        return TestimonialVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TestimonialVH) holder.bind(testimonials[position])
    }


    inner class TestimonialVH(v: View) : RecyclerView.ViewHolder(v) {

        fun bind(item: TestimonialItem) {

            itemView.run {
//                authorTV.setText(item.tstPersonName)
//                subTitleTV.setText(item.tstDesignation)
//                commentTV.setText(item.tstDetails)
//                if (item.tstPersonImage.isNullOrBlank()) profileIV.setImageResource(R.drawable.profile_dummy)
//                else picasso.load(item.tstPersonImage).fit().centerCrop().placeholder(placeholder).into(
//                    profileIV
//                )
//
//                var rating = 0f
//                try {
//                    rating = item.tstRating.toFloat()
//                } catch (e: Exception) {
//
//                }
//                feedbackRB.rating = rating
            }

        }
    }
}
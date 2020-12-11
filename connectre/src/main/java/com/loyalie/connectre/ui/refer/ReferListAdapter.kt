package com.loyalie.connectre.ui.refer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.ProgramItem
import com.loyalie.connectre.ui.offer.OfferDetailsActivity
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.convertToMMMDDYYY
import com.loyalie.connectre.util.loadUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.refer_list_item.view.*

class ReferListAdapter(
    private val contextIn: Context,
    private val programs: List<ProgramItem>,
    private val picasso: Picasso
) : PaginatingAdapter<ProgramItem>(contextIn, programs) {

    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.refer_list_item, parent, false)
        return ProgramVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProgramVH) holder.bind(programs[position])
    }


    inner class ProgramVH(v: View) : RecyclerView.ViewHolder(v) {

        init {
            itemView.setOnClickListener {
                val program = programs[adapterPosition]
                OfferDetailsActivity.start(
                    it.context, program.referral_id,
                    program.referral_url ?: "",
                    program.referral_name,
                    program.referral_description ?: ""
                )
            }
        }

        fun bind(item: ProgramItem) {
            itemView.run {
                if (item.referral_image.isNullOrBlank()) referIV.setImageResource(R.drawable.placeholder)
                else referIV.loadUrl(item.referral_image, picasso)
                referHeadingTV.setText(item.referral_name)
                descTV.setText(item.referral_description)
                validityTV.setText("Valid from " + item.referral_start_date.convertToMMMDDYYY() + " - " + item.referral_end_date.convertToMMMDDYYY())
            }
        }

    }
}
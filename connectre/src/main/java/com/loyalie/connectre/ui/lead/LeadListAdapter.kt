package com.loyalie.connectre.ui.lead

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loyalie.connectre.R
import com.loyalie.connectre.data.AssociatedProgramItem
import com.loyalie.connectre.ui.lead.details.LeadStatusDetailsActivity
import com.loyalie.connectre.util.PaginatingAdapter
import com.loyalie.connectre.util.loadUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.lead_list_item.view.*

class LeadListAdapter(
    private val contextIn: Context,
    private val programs: List<AssociatedProgramItem>,
    private val picasso: Picasso
) : PaginatingAdapter<AssociatedProgramItem>(contextIn, programs) {


    override fun getYourItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lead_list_item, parent, false)
        return LeadVH(v)
    }

    override fun bindYourViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LeadVH) holder.bind(programs[position])
    }


    inner class LeadVH(v: View) : RecyclerView.ViewHolder(v) {
        init {
            itemView.setOnClickListener {
                val program = programs[adapterPosition]
                LeadStatusDetailsActivity.start(
                    contextIn,
                    program.referral_id,
                    program.referral_name,
                    program.referral_description,
                    program.referral_image ?: ""
                )
            }
        }

        fun bind(item: AssociatedProgramItem) {
            itemView.run {
                if (item.referral_image.isNullOrBlank()) leadIV.setImageResource(R.drawable.placeholder)
                else leadIV.loadUrl(item.referral_image, picasso)
                leadHeadingTV.setText(item.referral_name)
                leadSubHeadingTV.setText(item.referral_description)
            }
        }
    }
}